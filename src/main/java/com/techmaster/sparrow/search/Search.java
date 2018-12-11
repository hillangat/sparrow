package com.techmaster.sparrow.search;

import com.techmaster.sparrow.entities.SearchResult;

public interface Search {

    SearchResult doSearch( SearchConfig adapterBean );

}
