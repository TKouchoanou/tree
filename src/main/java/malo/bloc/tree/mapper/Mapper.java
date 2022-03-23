package malo.bloc.tree.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class Mapper implements Dto2EntityMapper {
    private ModelMapper modelMapper=new ModelMapper();
    @Override
    public Object toEntity(Object o, Type s) {
        return modelMapper.map(o,s);
    }

    @Override
    public Object toDto(Object o, Type s) {
        return modelMapper.map(o,s);
    }

}
