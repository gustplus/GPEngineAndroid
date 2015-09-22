package com.gust.common.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.gust.common.game_util.GPLogger;
import com.gust.engine.core.GPShader;
import com.gust.engine.core.GPTextManager;
import com.gust.render_engine.core.GPTexture2D;
import com.gust.render_engine.core.GPTextureRegion;
import com.gust.scene2D.GPSprite;
import com.gust.system_implement.GPFileManager;

/**
 * 用于显示单行文本，无交互功能
 * 
 * @author gustplus
 * 
 */
public class GPLabel extends GPSprite {

	private String text;
	private float textSize;
	private int[] color;
	private Typeface font;
	
	private String fontName;

	public GPLabel(String text, String font, float textSize, float x, float y,
			GPShader shader) {
		super(x, y, shader);
		color = new int[] { 255, 255, 255, 255 };
		this.text = text;
		this.textSize = textSize;
		fontName = font;
		if (font != null && !font.equals("")) {
			fontName = "";
			try {
				this.font = Typeface.createFromAsset(GPFileManager
						.getinstance().getAssetManager(), font + ".ttf");
			} catch (Exception e) {
				GPLogger.log("GPLabel", "font can't be create", GPLogger.Error);
				font = null;
			}
		}
		if(text.length() == 0 || text == null){
			this.text = "";
		}
		GPTextureRegion region = drawText(text, textSize);
		setTextureRegion(region);
	}

	public GPLabel(String text, String font, float textSize, int[] color,
			float x, float y, GPShader shader) {
		super(x, y, shader);
		this.color = new int[] { color[0], color[1], color[2], color[3] };
		this.text = text;
		this.textSize = textSize;
		fontName = font;
		if (font != null && !font.equals("")) {
			fontName = "";
			try {
				this.font = Typeface.createFromAsset(GPFileManager
						.getinstance().getAssetManager(), font + ".ttf");
			} catch (Exception e) {
				GPLogger.log("GPLabel", "font can't be create", GPLogger.Error);
				font = null;
			}
		}
		GPTextureRegion region = drawText(text, textSize);
		setTextureRegion(region);
	}
	
	public void setFont(String font){
		if (font != null && !font.equals("")) {
			try {
				this.font = Typeface.createFromAsset(GPFileManager
						.getinstance().getAssetManager(), font + ".ttf");
			} catch (Exception e) {
				GPLogger.log("GPLabel", "font can't be create", GPLogger.Error);
				font = null;
			}
		}
	}

	public void setText(String text, float textSize) {
		if (text.equals(this.text) && textSize == this.textSize) {
			return;
		}
		this.text = text;
		this.textSize = textSize;
		GPTextureRegion region = drawText(text, textSize);
		setTextureRegion(region);
	}

	public void setText(String text) {
		if (text.equals(this.text)) {
			return;
		}
		this.text = text;
		GPTextureRegion region = drawText(text, textSize);
		setTextureRegion(region);
	}

	public void setTextSize(float textSize) {
		if (textSize == this.textSize) {
			return;
		}
		this.textSize = textSize;
		GPTextureRegion region = drawText(text, textSize);
		setTextureRegion(region);
	}

	public void setColor(int R, int G, int B, int A) {

		if (color[0] == R && color[1] == G && color[2] == B && color[3] == A) {
			return;
		}

		color[0] = R;
		color[1] = G;
		color[2] = B;
		color[3] = A;

		GPTextureRegion region = drawText(text, textSize);
		setTextureRegion(region);
	}

	public GPTextureRegion drawText(String text, float textSize) {
		GPTextureRegion region = null;
		String name = text + "/" + textSize + "/" + color[0] + "/" + color[1] + "/" + color[2]
				+ "/" + color[3] + "/" + fontName;
		region = GPTextManager.getInstance().getRegion(name);
		
		if(region != null){
			GPLogger.log("", "no ceate");
			return region;
		}
		
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setARGB(color[3], color[0], color[1], color[2]);
		if (font != null) {
			paint.setTypeface(font);
		}
		paint.setTextSize(textSize);
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);
		// 上下左右padding
		int realHeight = (int) (bounds.height() * 1.4f);
		int textWidth = getMinBoundLength(bounds.width() + 2);
		int textHeight = getMinBoundLength(realHeight);
		Bitmap map = Bitmap.createBitmap(textWidth, textHeight,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(map);
		canvas.drawText(text, 0, (int)(bounds.height() * 1.2f), paint);
		
		GPTexture2D tex = new GPTexture2D(text, map, true);

		region = new GPTextureRegion(text, tex, 0, 0,
				bounds.width() + 2, realHeight);
		GPTextManager.getInstance().addToCache(name, region);
		
		return region;
	}

	private int getMinBoundLength(int realHeight) {
		int base = 2;
		while (base < 2049) {
			if (realHeight < base) {
				return base;
			} else {
				base = base * 2;
			}
		}
		GPLogger.log("GPLabel", "string is too large!!!!", GPLogger.Error);
		return 0;
	}

	public void drawself() {
		if (presentation != null) {
			this.presentation.getTexture().bind();
			this.spriteRect.bind();
			this.spriteRect.draw();
		}
	}
	
	public String getText(){
		return text;
	}
}
