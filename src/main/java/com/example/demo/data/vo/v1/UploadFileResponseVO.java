package com.example.demo.data.vo.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileResponseVO implements Serializable {

    private String fileName;
    private String downloadURL;
    private String fileType;
    private long size;
}
