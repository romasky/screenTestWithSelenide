package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.ImageComparisonUtil;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;
import io.qameta.allure.Attachment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.OutputType;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ScreenTests {


    @Test
    public void testScreenIphone12Pro(TestInfo info) {
        Configuration.browserSize = "390x844";
        Selenide.open("https://www.google.com/");
        assertScreen(info);
    }

    private void assertScreen(TestInfo info) {
        String expectedFileName = info.getTestMethod().get().getName() + ".png";
        String expectedScreenDir = "src/test/resources/screens/";

        File actualScreenshot = Selenide.screenshot(OutputType.FILE);
        File expectedScreenshot = new File(expectedScreenDir+expectedFileName);

        if(!expectedScreenshot.exists()) {
            addImgToAllure("actual", actualScreenshot);
            throw new IllegalArgumentException("Can't assert image, because theres no reference." + "Actual screen can be downloaded from allure");

        }

        BufferedImage expectedImage = ImageComparisonUtil.readImageFromResources(expectedScreenDir + expectedFileName);
        BufferedImage actualImage = ImageComparisonUtil.readImageFromResources(actualScreenshot.toPath().toString());

        /**
         * Сейчас обозначим файл, который будет отвечать за нахождение различий между двумя этими картинками,
         * Если у нас что-то отличается, мы в финале увидим область выделенную красным цветом
         */

        File resultDestination = new File("build/diffs/diff_"+ expectedFileName);

        ImageComparison imageComparison = new ImageComparison(expectedImage, actualImage, resultDestination);
        ImageComparisonResult result = imageComparison.compareImages();

        if(!result.getImageComparisonState().equals(ImageComparisonState.MATCH)) {
            addImgToAllure("actual", actualScreenshot);
            addImgToAllure("expected", expectedScreenshot);
            addImgToAllure("diff", resultDestination);
        }

        Assertions.assertEquals(ImageComparisonState.MATCH,  result.getImageComparisonState());
    }

    private void addImgToAllure(String name, File file) {         //Передаваемый файл превратит в байты и добавит в Allure отчет, для удобного использования
        try {
            byte[] image = Files.readAllBytes(file.toPath());
            saveScreenshot(name, image);
        }
        catch (IOException e) {
            throw new RuntimeException("Can't read bytes");

        }

    }

    @Attachment(value = "{name}", type="image/png")                      //Метод, который через аннотацию добавит текущий файл в Allure отчет
    private static byte[] saveScreenshot(String name, byte[] image) {
        return image;
    }





}
