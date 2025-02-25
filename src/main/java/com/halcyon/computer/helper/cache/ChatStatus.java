package com.halcyon.computer.helper.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatStatus {
    private ChatStatusType type;
    private List<String> data;

    public ChatStatus(ChatStatusType type) {
        this.type = type;
    }
}
