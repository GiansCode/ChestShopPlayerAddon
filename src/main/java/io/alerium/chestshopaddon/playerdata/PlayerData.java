package io.alerium.chestshopaddon.playerdata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor @Getter
public class PlayerData {

    private final UUID uuid;
    @Setter private int shops;

}
