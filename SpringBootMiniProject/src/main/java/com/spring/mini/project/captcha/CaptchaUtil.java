package com.spring.mini.project.captcha;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.backgrounds.GradiatedBackgroundProducer;
import cn.apiclub.captcha.text.producer.DefaultTextProducer;
import cn.apiclub.captcha.text.renderer.DefaultWordRenderer;

public class CaptchaUtil {

	public static Captcha createCaptcha(int width, int height) {
		
		return new Captcha.Builder(width,height)
				.addBackground(new GradiatedBackgroundProducer(Color.BLUE,Color.white))
				.addText(new DefaultTextProducer(),new DefaultWordRenderer())
				.addNoise()
				.build()
				;
	}

	public static String encodeBase64(Captcha captcha) {
		
		String image = null;
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(captcha.getImage(), "png", os);
			byte[] arr = Base64.getEncoder().encode(os.toByteArray());
			image = new String(arr);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return image;
	}
}
