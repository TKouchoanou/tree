package malo.bloc.tree.controller;

import malo.bloc.tree.dtos.TreeDto;
import malo.bloc.tree.dtos.UserDto;
import malo.bloc.tree.dtos.create.NewNodeLeafDto;
import malo.bloc.tree.dtos.create.NewTreeDto;
import malo.bloc.tree.dtos.error.ErrorDtoInterface;
import malo.bloc.tree.mapper.tree.TreeDto2EntityMapper;
import malo.bloc.tree.persistence.entity.Tree;
import malo.bloc.tree.persistence.entity.User;
import malo.bloc.tree.service.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class TreeController {
    @Autowired
    private TreeService treeService;

    @Autowired
    TreeDto2EntityMapper mapper;

    @Autowired
    ErrorDtoInterface error;



    @PostMapping(value= "user/{userid}/add/child/tree/{treeId}")
    public TreeDto addChild(@PathVariable ("userid")  int uid ,@PathVariable ("treeId")  int treeId, @RequestBody TreeDto treeDto){
        Tree tree =  mapper.toEntity(treeDto);
        Tree savedTree= treeService.addChildForUserTree(uid,treeId,tree);
        return  mapper.toDto(savedTree);
    }

    @PostMapping(value= "user/{userid}/add/children/tree/{treeId}")
    public TreeDto addChildren(@PathVariable ("userid")  int uid , @PathVariable ("treeId")  int treeId, @RequestBody Collection<TreeDto> treeDtoList){
        Collection<Tree> children = treeDtoList.stream().map(treeDto->mapper.toEntity(treeDto)).collect(Collectors.toList());
        Tree savedTree= treeService.addChildrenForUserTree(uid,treeId,children);
        return  mapper.toDto(savedTree);
    }

    @PostMapping(value= "user/{userid}/add/tree")
    public User addRoot(@PathVariable ("userid")  int uid , @RequestBody NewTreeDto treeDto){
        return treeService.addTreeToUser(uid, mapper.toEntity(treeDto))  ;
    }

    @GetMapping(value= "/tree/{treeId}")
    public Tree show(@PathVariable ("treeId")  int id){
        return treeService.getTreeById(id).get();
    }

    @PutMapping(value= "user/{userid}/update/tree")
    public TreeDto update(@PathVariable ("userid")  int uid , @RequestBody TreeDto treeDto){
       Tree tree= treeService.updateUserTree(uid,mapper.toEntity(treeDto) ) ;
       return mapper.toDto(tree);
    }

    @DeleteMapping(value= "user/{userId}/delete/tree/{treeId}")
    public ResponseEntity<Object> delete(@PathVariable ("userId")  int uid , @PathVariable ("treeId")  int treeId){
        try {
            Optional<Tree> tree = treeService.delete(uid,treeId);
            return tree.map(value -> new ResponseEntity<Object>(mapper.toDto(value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(error.setMessage("Failed to delete tree with id =" + treeId), HttpStatus.OK));
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().
                    contentType(MediaType.APPLICATION_JSON).
                    body(error.setMessage(e.getMessage()));
        }
    }


}
