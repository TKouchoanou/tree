package malo.bloc.tree.dtos;
import java.util.Set;

public abstract  class PartialUserDto {
    public abstract Set<String> getRoleNames();
    public abstract PartialUserDto setRoleNames( Set<String> rolesNames);
}
