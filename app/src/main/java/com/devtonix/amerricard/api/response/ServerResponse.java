package com.devtonix.amerricard.api.response;

import java.io.Serializable;
import java.util.List;

public class ServerResponse<T> implements Serializable {
        public String status;
        public String error;
        public List<T> data;
}
