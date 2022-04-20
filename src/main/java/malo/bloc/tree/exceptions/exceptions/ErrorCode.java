package malo.bloc.tree.exceptions.exceptions;

public enum ErrorCode {
    UNKNOW_ERROR_EXCEPTION(0000),
    SOME_PROBLEM_OCCUR(0001),
    UNSUPPORTED_FORMAT(1000),
    //empty id
    EMPTY_ID(2000),
    EMPTY_TREE_ID(2001),
    EMPTY_USER_ID(2002),
    EMPTY_LEAF_ID(2003),

    // not owner
    NOT_RESOURCE_OWNER(3000),
    NOT_TREE_OWNER(3001),
    NOT_TREE_LEAF_OWNER(3002),

    // not found
    ENTITY_NOT_FOUND(4000),
    ENTITY_USER_NOT_FOUND(4001),
    ENTITY_TREE_NOT_FOUND(4002),
    ENTITY_LEAF_NOT_FOUND(4003),

    //not valid
    INVALID_ENTITY(6000),
    INVALID_ENTITY_USER(6001),
    INVALID_ENTITY_TREE(6002);

    ErrorCode(int i) {
    }
}

