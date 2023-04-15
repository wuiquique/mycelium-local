package com.mycelium.local.dynamic.search;

import java.util.List;

public interface SearchCriteria {

    public class TextContains implements SearchCriteria {
        public final String value;

        public TextContains(String value) {
            this.value = value;
        }
    }

    public class PriceComparison implements SearchCriteria {
        public final int value;
        public final boolean gteq;

        public PriceComparison(int value, boolean gteq) {
            this.value = value;
            this.gteq = gteq;
        }
    }

    public class CategoryIn implements SearchCriteria {
        public final List<Integer> ids;

        public CategoryIn(List<Integer> ids) {
            this.ids = ids;
        }
    }
}
