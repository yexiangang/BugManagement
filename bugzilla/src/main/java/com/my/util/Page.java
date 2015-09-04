package com.my.util;

import java.util.ArrayList;
import java.util.List;

public class Page<T>
{
	private int curPage;
	private int pageSize;
	private int pageCount;
	private long itemCount;
	private int fromIndex;
	private int toIndex;
	private List<T> curItems;
	private int curItemCount;
	private int showPageCount = 10;
	private List<Integer> showPageIndices = new ArrayList<Integer>(10);
	
	public Page(int curPage, int pageSize, List<T> items)
	{
		this(curPage, pageSize, items.size());
		
		curItems = items.subList(fromIndex, toIndex);
	}
	
	public Page(int curPage, int pageSize, long itemCount)
	{
		this.curPage = curPage;
		this.pageSize = pageSize;
		this.itemCount = itemCount;
		this.pageCount = (int) ((itemCount + pageSize - 1) / pageSize);
		
		fromIndex = (curPage - 1) * pageSize;
		if (fromIndex < 0 || fromIndex >= itemCount)
		{
			curPage = 1;
			fromIndex = 0;
		}
		toIndex = (int) Math.min(fromIndex + pageSize, itemCount);
		curItemCount = toIndex - fromIndex;
		
		int start = curPage - curPage % showPageCount + 1;
		int end = Math.min(curPage + showPageCount, pageCount + 1); //enclusive
		for (int i = start; i < end; ++i)
		{
			showPageIndices.add(i);
		}
	}
	
	public int getCurItemCount()
	{
		return curItemCount;
	}

	public void setCurItemCount(int curItemCount)
	{
		this.curItemCount = curItemCount;
	}

	public int getFromIndex()
	{
		return fromIndex;
	}

	public void setFromIndex(int fromIndex)
	{
		this.fromIndex = fromIndex;
	}

	public int getToIndex()
	{
		return toIndex;
	}

	public void setToIndex(int toIndex)
	{
		this.toIndex = toIndex;
	}

	public int getCurPage()
	{
		return curPage;
	}

	public void setCurPage(int curPage)
	{
		this.curPage = curPage;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	public int getPageCount()
	{
		return pageCount;
	}

	public void setPageCount(int pageCount)
	{
		this.pageCount = pageCount;
	}

	public long getItemCount()
	{
		return itemCount;
	}

	public void setItemCount(long itemCount)
	{
		this.itemCount = itemCount;
	}

	public List<T> getCurItems()
	{
		return curItems;
	}

	public void setCurItems(List<T> curItems)
	{
		this.curItems = curItems;
	}

	public List<Integer> getShowPageIndices()
	{
		return showPageIndices;
	}

	public void setShowPageIndices(List<Integer> showPageIndices)
	{
		this.showPageIndices = showPageIndices;
	}
	
	
}
