package com.wildbean.wastools.comp;

public abstract interface TreeFilter
{
  public abstract boolean pass(Object paramObject);
  
  public abstract void setFiltered(boolean paramBoolean);
  
  public abstract boolean isFiltered();
  
  public abstract void setIncluded(String paramString);
}


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\wildbean\wastools\comp\TreeFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */