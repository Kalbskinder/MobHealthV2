package net.kalbskinder.mobHealth.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class TextUtil {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final LegacyComponentSerializer LEGACY =
            LegacyComponentSerializer.builder()
                    .character('&')
                    .hexColors()
                    .useUnusualXRepeatedCharacterHexFormat()
                    .build();

    public String legacyToMiniMessage(String input) {
        Component component = LEGACY.deserialize(input);
        return miniMessage.serialize(component);
    }

    public String parseLegacy(String input) {
        return input.replace("&", "§");
    }

    public Component parse(String input) {
        return miniMessage.deserialize(input);
    }
}
