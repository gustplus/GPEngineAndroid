package com.gust.scene2D.TileMap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import com.gust.engine.core.GPSourceManager;
import com.gust.render_engine.base.MatrixHelper;
import com.gust.scene2D.GPLayer2D;
import com.gust.scene2D.GPSpriteBatchNode;

public class TileLayer extends GPLayer2D{
	private TileMap map;
	
	private String name;
	
	private static byte[] base64;
	
	private int width;
	private int height;
	
	private boolean totalMove;
	
	private boolean flipped_horizontally;
	private boolean flipped_vertically;
	private boolean flipped_diagonally;
	
	private final int FLIPPED_HORIZONTALLY_FLAG = 0x80000000;
	private final int FLIPPED_VERTICALLY_FLAG   = 0x40000000;
	private final int FLIPPED_DIAGONALLY_FLAG   = 0x20000000;
	
	private HashMap<Integer, GPSpriteBatchNode> layers;

	
	public TileLayer(TileMap map, String name, int width, int height) {
		super(0, 0, 0, 0);
		layers = new HashMap<Integer, GPSpriteBatchNode>(2);
		this.map = map;
		this.name = name;
		this.width = width;
		this.height = height;
		totalMove = false;
	}
	
	public String getName() {
		return name;
	}

	public void setDataOfBase64(String data, String encoding, String compression){
		if (base64 == null) {
			base64 = new byte[256];
			for (int i = 0; i < 256; i++) {
				base64[i] = -1;
			}
			for (int i = 'A'; i <= 'Z'; i++) {
				base64[i] = (byte) (i - 'A');
			}
			for (int i = 'a'; i <= 'z'; i++) {
				base64[i] = (byte) (26 + i - 'a');
			}
			for (int i = '0'; i <= '9'; i++) {
				base64[i] = (byte) (52 + i - '0');
			}
			base64['+'] = 62;
			base64['/'] = 63;
		}
		
		if ("base64".equals(encoding) && "gzip".equals(compression)) {
			try {
				char[] enc = data.toCharArray();
				byte[] dec = decodeBase64(enc);
				GZIPInputStream is = new GZIPInputStream(
						new ByteArrayInputStream(dec));

				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						int tileId = 0;
						tileId |= is.read();
						tileId |= is.read() << 8;
						tileId |= is.read() << 16;
						tileId |= is.read() << 24;

						int size = map.getTileSetNum() - 1;
					    for (int i = size; i >= 0; --i) {
					      TileSet tileset = map.getTileSet(i);
					      if (tileset.getFirstGid() <= tileId) {
					    	  int realIndex = tileId - tileset.getFirstGid(); 
					    	  if(realIndex > -1){
								   GPSpriteBatchNode layer = this.layers.get(realIndex);
								   if(layer == null){
									   layer = new GPSpriteBatchNode(GPSourceManager.getInstance().preloadShader("tex"));
									   this.addChild(layer);
									   layers.put(realIndex, layer);
								   }
								   layer.addChild(tileset.getTile(x, y, map.getWidthNum(), map.getHeightNum(), realIndex));
								   break;
					    	  }
					      }
					    }
					}
				}
			} catch (IOException e) {
				throw new RuntimeException("Unable to decode base64 !");
			}
		} else {
			throw new RuntimeException("Unsupport tiled map type " + encoding
					+ "," + compression + " only gzip base64 Support !");
		}
	}
	
	private byte[] decodeBase64(char[] data) {
		int temp = data.length;
		for (int ix = 0; ix < data.length; ix++) {
			if ((data[ix] > 255) || base64[data[ix]] < 0) {
				--temp;
			}
		}
		int len = (temp / 4) * 3;
		if ((temp % 4) == 3) {
			len += 2;
		}
		if ((temp % 4) == 2) {
			len += 1;
		}
		byte[] out = new byte[len];

		int shift = 0;
		int accum = 0;
		int index = 0;

		for (int ix = 0; ix < data.length; ix++) {
			int value = (data[ix] > 255) ? -1 : base64[data[ix]];

			if (value >= 0) {
				accum <<= 6;
				shift += 6;
				accum |= value;
				if (shift >= 8) {
					shift -= 8;
					out[index++] = (byte) ((accum >> shift) & 0xff);
				}
			}
		}

		if (index != out.length) {
			throw new RuntimeException("index != " + out.length);
		}

		return out;
	}
	
	public void setDataOfXML(int[] datas){
		int tileIndex = 0;
		int size = map.getTileSetNum() - 1;
		for(int y = 0; y < height; ++y){
			for(int x = 0; x < width; ++x){
				int globalTileId = datas[tileIndex++];
			    for(int i = size; i >= 0; --i) {
				   TileSet tileset = map.getTileSet(i);
				   int tileId = tileset.getFirstGid();
				   if (tileId <= globalTileId) {
					   int realIndex = globalTileId - tileset.getFirstGid(); 
					   if(realIndex > -1){
					   GPSpriteBatchNode layer = this.layers.get(tileId);
					   if(layer == null){
						   layer = new GPSpriteBatchNode(GPSourceManager.getInstance().preloadShader("gui"));
						   this.addChild(layer);
						   layers.put(tileId, layer);
					   }
						   layer.addChild(tileset.getTile(x, y, map.getWidthNum(), map.getHeightNum(), realIndex));
						   break;
					   }
				   }
			   }
			}
		}
	}
	
	@Override
	public void beforeDraw() {
		if(totalMove){
		MatrixHelper.getInstance().storeMatrix(MatrixHelper.getInstance().getTotalMatrix());
		MatrixHelper.getInstance().setTotalMatrix(MatrixHelper.getInstance().getTotalMatrix().mul(this.worldTransform));
		}
	}

	@Override
	public void afterDraw() {
		if(totalMove){
			MatrixHelper.getInstance().setTotalMatrix(
				MatrixHelper.getInstance().loadMatrix());
		}
	}

	@Override
	public void updateGeometryState() {
		if(position.x != 0 && position.y != 0 ){
			return;
		}else{
			totalMove = false;
		}
		
		this.updateTransform();
		this.updateWorldTransform();
		this.updateWorldPosition();
		if (children != null && ! totalMove) {
			int len = children.size();
			for (int i = 0; i < len; ++i) {
				children.get(i).updateGeometryState();
			}
		}
		worldIsCurrent = true;
//			this.calculateBoundingBox();
		modelIsCurrent = true;
		totalMove = true;
	}
}
