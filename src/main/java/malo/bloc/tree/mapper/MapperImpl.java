package malo.bloc.tree.mapper;

import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
@Component
public class MapperImpl implements Dto2EntityMapper{
    @Override
    public Object toEntity(Object o, Type s) {
        return null;
    }

    @Override
    public Object toDto(Object o, Type s) {
        return null;
    }
}
