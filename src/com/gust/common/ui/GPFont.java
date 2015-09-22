package com.gust.common.ui;

import com.gust.common.game_util.GPLogger;
import com.gust.engine.core.GPShader;
import com.gust.engine.core.GPTextureManager;
import com.gust.render_engine.core.GPTexture2D;
import com.gust.render_engine.core.GPTextureRegion;
import com.gust.scene2D.GPSprite;
import com.gust.scene2D.GPSpriteBatchNode;

public class GPFont extends GPSpriteBatchNode {
	private GPTexture2D words;
	private char startWord;
	private int perHeight;
	private int perWidth;
	private int rows;
	private int cols;

	public GPFont(GPTexture2D tex, char startWord, int perWidth, int perHeight,
			int rows, int cols, GPShader shader) {
		super(shader);
		words = tex;
		bind(startWord, perWidth, perHeight, rows, cols);
	}

	public GPFont(String name, char startWord, int perWidth, int perHeight,
			int rows, int cols, GPShader shader) {
		super(shader);
		words = GPTextureManager.getInstance().loadTexture2D(name, true);
		bind(startWord, perWidth, perHeight, rows, cols);
	}

	private void bind(char startWord, int perWidth, int perHeight, int rows,
			int cols) {
		this.startWord = startWord;
		this.perHeight = perHeight;
		this.perWidth = perWidth;
		this.cols = cols;
		this.rows = rows;
	}

	/**
	 * 
	 * @param startCenter文字第一个字的中心点
	 * @param words
	 * @param height文字高度
	 * @param interval文字间间隔
	 */
	public void setText(String words, float interval) {
		int length = words.length();
		int offset = 0;
		int row;
		int col;
		GPTextureRegion region;
		float x = perWidth / 2;
		removeAllChildrenWithCleanUp();
		for (int i = 0; i < length; i++) {
			offset = words.charAt(i);
			offset -= startWord;
			row = (int) (offset / cols);
			if (row > rows)
				GPLogger.log("", "the '" + words.charAt(i)
						+ "' is off the bound");
			col = offset % cols;
			region = new GPTextureRegion("", this.words, col * perWidth, row
					* perHeight, perWidth, perHeight);
			GPSprite text = new GPSprite(region, x, perHeight / 2, shader);
			addChild(text);
			x += (perWidth + interval);
		}
	}

	// public void drawWithLeftBottom(GPVector2f leftBottom, String words,
	// float height, float interval) {
	// GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	// this.batcher.beginBatch(this.words);
	// int length = words.length();
	// int offset = 0;
	// int row;
	// int col;
	// float width = height * ratio;
	// GPTextureRegion region;
	// float x = leftBottom.x + width / 2;
	// for (int i = 0; i < length; i++) {
	// offset = words.charAt(i);
	// offset -= startWord;
	// row = (int) (offset / cols);
	// if (row > rows)
	// GPLogger.log("", "the '" + words.charAt(i)
	// + "' is off the bound");
	// col = offset % cols;
	// region = new GPTextureRegion(this.words, col * perWidth, row
	// * perHeight, perWidth, perHeight);
	// this.batcher.drawSprite(x, leftBottom.y + height / 2, width,
	// height, region);
	// x += (width + interval);
	// }
	// this.batcher.endBatch();
	// }
}
