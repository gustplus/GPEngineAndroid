package com.gust.common.ui;

import java.util.ArrayList;

import com.gust.common.game_util.GPLogger;
import com.gust.common.game_util.GPPool;
import com.gust.common.game_util.GPPool.PoolObjectFactory;
import com.gust.common.ui.GPScrollView.GPScrollViewAdpter;
import com.gust.physics2D.Collision_System2D;
import com.gust.scene2D.GPLayer2D;

public class GPTableView extends GPScrollView implements GPScrollViewAdpter {

	public static interface GPTableViewAdpter {
		public void getCellNum();

		public void createCell(int index);
	}

	static class Cell extends GPLayer2D {
		public Cell(float x, float y, float width, float height) {
			super(x, y, width, height);
		}
	}

	private GPPool<Cell> pool;

	private GPTableViewAdpter tableAdpter;

	private ArrayList<Cell> cells;

	public GPTableView(float x, float y, float width, float height) {
		super(x, y, width, height, ScrollType.ScrollType_V);
		PoolObjectFactory<Cell> factory = new PoolObjectFactory<GPTableView.Cell>() {
			public Cell createObject() {
				return new Cell(0, 0, 0, 0);
			}
		};
		pool = new GPPool<GPTableView.Cell>(factory, 10);
		setContainer(new GPLayer2D(0, 0, width, height));
	}

	public void setScrollAdpter(GPScrollViewAdpter scrollAdpter) {
		GPLogger.log("GPTableView", "can't set ScrollViewAdpter for tableview");
	}

	public void setTableViewAdpter(GPTableViewAdpter adpter) {
		this.tableAdpter = adpter;
	}

	public Cell getUnusedCell() {
		return pool.newObject();
	}

	public void onScroll(GPScrollView view) {
		int size = cells.size();
		for (int i = 0; i < size; ++i) {
			Cell cell = cells.get(i);
			cell.move(movement);
			if (!Collision_System2D.isCollideRectangles(cell.getBoundBox(),
					this.contentBound)) {
				pool.free(cell);
			}
		}
	}

	public void onZoom(GPScrollView view) {}

	public void onFix(GPScrollView view) {
		
	}
}