package com.example.krisinoteBackend.sync;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceDiff<T> {
    private DiffType diffType;
    private T resource;
}
