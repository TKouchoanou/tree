package malo.bloc.tree.mapper;

import java.lang.reflect.Type;

public interface Dto2EntityMapper <S,D>{
    D toEntity(S o, Type s);
    S toDto(D o,Type s);
}
