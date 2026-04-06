package j2ee.BuiTanThanh.constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {
    ADMIN(1),
    USER(2);

    public final long value;
}
