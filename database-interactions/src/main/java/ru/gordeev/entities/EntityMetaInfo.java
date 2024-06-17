package ru.gordeev.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class EntityMetaInfo {
    private EntityField id;
    private List<EntityField> fields;
}
