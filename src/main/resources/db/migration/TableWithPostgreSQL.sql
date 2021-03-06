

CREATE TABLE role (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   name VARCHAR(255) NOT NULL,
   updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE "user" (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   first_name VARCHAR(255) NOT NULL,
   last_name VARCHAR(255) NOT NULL,
   email VARCHAR(100) NOT NULL,
   password VARCHAR(255),
   country VARCHAR(100),
   city VARCHAR(100),
   adresse VARCHAR(150),
   updated_at TIMESTAMP WITHOUT TIME ZONE,
   created_at TIMESTAMP WITHOUT TIME ZONE,
   CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE "user" ADD CONSTRAINT uc_user_email UNIQUE (email);

CREATE TABLE user_role (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   user_id INTEGER,
   role_id INTEGER,
   updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   CONSTRAINT pk_user_role PRIMARY KEY (id)
);

ALTER TABLE user_role ADD CONSTRAINT FK_USER_ROLE_ON_ROLE FOREIGN KEY (role_id) REFERENCES role (id);

ALTER TABLE user_role ADD CONSTRAINT FK_USER_ROLE_ON_USER FOREIGN KEY (user_id) REFERENCES "user" (id);


CREATE TABLE tree (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   parent_id INTEGER,
   name VARCHAR(255),
   user_id INTEGER,
   updated_at TIMESTAMP WITHOUT TIME ZONE,
   created_at TIMESTAMP WITHOUT TIME ZONE,
   CONSTRAINT pk_tree PRIMARY KEY (id)
);

ALTER TABLE tree ADD CONSTRAINT FK_TREE_ON_PARENT FOREIGN KEY (parent_id) REFERENCES tree (id);

ALTER TABLE tree ADD CONSTRAINT FK_TREE_ON_USER FOREIGN KEY (user_id) REFERENCES "user" (id);

CREATE TABLE node_leaf (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   tree_id INTEGER NOT NULL,
   title VARCHAR(255) NOT NULL,
   updated_at TIMESTAMP WITHOUT TIME ZONE,
   created_at TIMESTAMP WITHOUT TIME ZONE,
   CONSTRAINT pk_node_leaf PRIMARY KEY (id)
);

ALTER TABLE node_leaf ADD CONSTRAINT FK_NODE_LEAF_ON_TREE FOREIGN KEY (tree_id) REFERENCES tree (id);

CREATE TABLE metadata (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   leaf_id INTEGER NOT NULL,
   name VARCHAR(255) NOT NULL,
   value TEXT NOT NULL,
   updated_at TIMESTAMP WITHOUT TIME ZONE,
   CONSTRAINT pk_metadata PRIMARY KEY (id)
);

ALTER TABLE metadata ADD CONSTRAINT FK_METADATA_ON_LEAF FOREIGN KEY (leaf_id) REFERENCES node_leaf (id);

CREATE TABLE link (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   leaf_id INTEGER NOT NULL,
   name VARCHAR(255),
   value VARCHAR(255) NOT NULL,
   updated_at TIMESTAMP WITHOUT TIME ZONE,
   CONSTRAINT pk_link PRIMARY KEY (id)
);

ALTER TABLE link ADD CONSTRAINT FK_LINK_ON_LEAF FOREIGN KEY (leaf_id) REFERENCES node_leaf (id);






