package org.daijie.core.kisso.captcha;

import java.awt.image.BufferedImage;

import com.baomidou.kisso.common.captcha.background.LineNoiseBackgroundFactory;
import com.baomidou.kisso.common.captcha.color.RandomColorFactory;
import com.baomidou.kisso.common.captcha.filter.predefined.WobbleRippleFilterFactory;
import com.baomidou.kisso.common.captcha.font.RandomFontFactory;
import com.baomidou.kisso.common.captcha.font.UpperRandomWordFactory;
import com.baomidou.kisso.common.captcha.service.AbstractCaptchaService;
import com.baomidou.kisso.common.captcha.service.Captcha;
import com.baomidou.kisso.common.captcha.text.renderer.RandomYBestFitTextRenderer;

/**
 * 初始化默认图形验证码，排除数字与字母近似的字符
 * @author daijie_jay
 * @since 2017年12月1日
 */
public class KissoCaptchaService extends AbstractCaptchaService {

	public KissoCaptchaService() {
		backgroundFactory = new LineNoiseBackgroundFactory();
		wordFactory = new UpperRandomWordFactory();
		fontFactory = new RandomFontFactory();
		textRenderer = new RandomYBestFitTextRenderer();
		colorFactory = new RandomColorFactory();
		filterFactory = new WobbleRippleFilterFactory();
		textRenderer = new RandomYBestFitTextRenderer();
	}

	@Override
	public Captcha getCaptcha() {
		BufferedImage bufImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		backgroundFactory.fillBackground(bufImage);
		String word = wordFactory.getNextWord();
		word = word.replaceAll("0", "8").replaceAll("o", "p").replaceAll("O", "R").replaceAll("l", "L");
		textRenderer.draw(word, bufImage, fontFactory, colorFactory);
		bufImage = filterFactory.applyFilters(bufImage);
		return new Captcha(word, bufImage);
	}
}
