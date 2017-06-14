package com.devtonix.amerricard.network.response;

import java.io.Serializable;
import java.util.List;

public class ServerResponse<T> implements Serializable {
        public String status;
        public String error;
        public List<T> data;
}
