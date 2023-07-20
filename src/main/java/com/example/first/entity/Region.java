package com.example.first.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class Region {
    String regionParent;
    String regionChild;
    String nx;
    String ny;

    @Builder
    public Region(String regionParent, String regionChild, String nx, String ny) {
        this.regionParent = regionParent;
        this.regionChild = regionChild;
        this.nx = nx;
        this.ny = ny;
    }
}