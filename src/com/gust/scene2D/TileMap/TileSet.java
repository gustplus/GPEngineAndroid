package com.gust.scene2D.TileMap;

import com.gust.common.game_util.GPLogger;
import com.gust.common.math.GPVector2f;
import com.gust.engine.core.GPSourceManager;
import com.gust.render_engine.core.GPTexture2D;
import com.gust.render_engine.core.GPTextureRegion;
import com.gust.scene2D.GPSprite;

public class TileSet {
	private int first_gid;

	private GPTexture2D tileTex;
	// private GPTexture_20 tileTex;

	private int texWidth;

	private int tileWidth;
	private int tileHeight;

	private int spacing; // 图块的间距，在原图上采样图块时，图块与图块之间的间隔
	private int margin; // 图块的边距，在原图上采样图块时，图像左侧与上方采样的剔除边界大小

	private int tileoffsetX;
	private int tileoffsetY;

	public TileSet(int first_gid, int tileWidth, int tileHeight) {
		this.first_gid = first_gid;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.spacing = 0;
		this.margin = 0;
	}

	public void setTexture(String name) {
		int position = name.lastIndexOf("/");
		String fileName = name.substring(position + 1);
		this.tileTex = GPSourceManager.getInstance().preloadTexture(fileName,
				true);
		texWidth = tileTex.width / tileWidth;
		GPLogger.log("", "tex = " + fileName);
	}

	public int getFirstGid() {
		return first_gid;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}

	public void setTileOffset(int x, int y) {
		this.tileoffsetX = x;
		this.tileoffsetY = y;
	}

	public GPSprite getTile(int x, int y, int layerWidth, int layerHeight,
			int index) {
		GPSprite tile;
		GPTextureRegion region;
		int cX = (int) ((this.tileWidth) * (x + 0.5f));
		int cY = (int) ((this.tileHeight) * (layerHeight - y - 0.5f));
		int startX = (this.tileWidth + this.spacing) * (index % texWidth);
		int startY = (this.tileHeight + this.spacing) * (index / texWidth);
		region = new GPTextureRegion("", this.tileTex, startX, startY, tileWidth
				- margin * 2, tileHeight - margin * 2);
		tile = new GPSprite(region, cX, cY, GPSourceManager
				.getInstance().preloadShader("gui"));
		return tile;
	}
}
