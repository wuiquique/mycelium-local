package com.mycelium.local.dynamic.search;

import java.util.List;

public interface SearchCriteria {

    public class TextContains implements SearchCriteria {
        public final String value;

        public TextContains(String value) {
            this.value = value;
        }
    }

    public class PriceBetween implements SearchCriteria {
        public final int min;
        public final int max;

        public PriceBetween(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }

    public class CategoryIn implements SearchCriteria {
        public final List<Integer> ids;

        public CategoryIn(List<Integer> ids) {
            this.ids = ids;
        }
    }
}
