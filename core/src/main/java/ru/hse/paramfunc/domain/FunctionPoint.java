package ru.hse.paramfunc.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class FunctionPoint {

    @NonNull
    private int t;

    private float systemX;
    private float systemY;
    private float systemZ;

    @NonNull
    private float originalX;
    @NonNull
    private float originalY;
    @NonNull
    private float originalZ;

}
