package malo.bloc.tree.controller;

import malo.bloc.tree.dtos.TreeDto;
import malo.bloc.tree.dtos.create.NewTreeDto;
import malo.bloc.tree.dtos.error.ErrorDto;
import malo.bloc.tree.exceptions.exceptions.NotResourceOwnerException;
import malo.bloc.tree.exports.FormatExporterFactory;
import malo.bloc.tree.mapper.tree.TreeDto2EntityMapper;
import malo.bloc.tree.persistence.entity.Tree;
import malo.bloc.tree.persistence.entity.User;
import malo.bloc.tree.persistence.repository.TreeRepository;
import malo.bloc.tree.search.repository.UserRepository;
import malo.bloc.tree.service.UserService;
import malo.bloc.tree.service.UserTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class TreeController {
    @Autowired
    private UserTreeService userTreeService;
    @Autowired
    private UserService userService;
    @Autowired
    private TreeRepository treeRepository;

    @Autowired
    TreeDto2EntityMapper mapper;

    @Autowired
    FormatExporterFactory<User> exporterFactory;

    @Autowired
    @Qualifier("main")
    UserRepository userSearchRepository;

    @GetMapping(value = "/users/trees")
    public Iterable<Tree> userTrees(){
        return  treeRepository.findAll();
    }

    @PostMapping(value= "user/{userid}/add/child/tree/{treeId}")
    public TreeDto addChild(@PathVariable ("userid")  int uid ,@PathVariable ("treeId")  int treeId, @RequestBody TreeDto treeDto) throws NotResourceOwnerException {
        return  mapper.toDto(userTreeService.addChildForUserTree(uid,treeId,mapper.toEntity(treeDto)));
    }

    @PostMapping(value= "user/{userid}/add/children/tree/{treeId}")
    public TreeDto addChildren(@PathVariable ("userid")  int uid , @PathVariable ("treeId")  int treeId, @RequestBody Collection<TreeDto> treeDtoList) throws NotResourceOwnerException {
        Collection<Tree> children = treeDtoList.stream().map(treeDto->mapper.toEntity(treeDto)).collect(Collectors.toList());
        Tree savedTree= userTreeService.addChildrenForUserTree(uid,treeId,children);
        return  mapper.toDto(savedTree);
    }

    @PostMapping(value= "user/{userid}/add/tree")
    public User addRoot(@PathVariable ("userid")  int uid , @RequestBody NewTreeDto treeDto){
        return userTreeService.addTreeToUser(uid, mapper.toEntity(treeDto))  ;
    }

    @GetMapping(value= "user/{userid}/tree/{treeId}")
    public Tree show(@PathVariable ("userid")  int uid,@PathVariable ("treeId")  int id ) throws NotResourceOwnerException {
        return userTreeService.findById(uid,id).get();
    }

    @PutMapping(value= "user/{userid}/update/tree")
    public TreeDto update(@PathVariable ("userid")  int uid , @RequestBody TreeDto treeDto) throws NotResourceOwnerException {
        Tree tree= userTreeService.updateUserTree(uid,mapper.toEntity(treeDto) ) ;
       return mapper.toDto(tree);
    }

    @DeleteMapping(value= "user/{userId}/delete/tree/{treeId}")
    public ResponseEntity<Object> delete(@PathVariable ("userId")  int uid , @PathVariable ("treeId")  int treeId) throws NotResourceOwnerException {
        Optional<Tree> tree = userTreeService.deleteUserTree(uid,treeId);
        return tree.map(value -> new ResponseEntity<Object>(mapper.toDto(value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>((new ErrorDto()).setMessage("Failed to delete tree with id ="+treeId), HttpStatus.NOT_MODIFIED));

    }

    @GetMapping(value= "user/{userid}/search")
    private Optional<malo.bloc.tree.search.bean.User> test(TreeDto treeDto){
       userSearchRepository.findAll().forEach(v->System.out.println(v.getId()));
        return userSearchRepository.findById(""+treeDto.getId());
    }

    @GetMapping("/users/export/{format}")
    public void exportToExcel(HttpServletResponse response,@PathVariable String format) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + "."+ exporterFactory.getExtension(format);
        response.setHeader(headerKey, headerValue);
        List<User> users =
                StreamSupport.stream(userService.getAllUsers().spliterator(),false)
                        .collect(Collectors.toList());
        exporterFactory.getExporterFactory(format).getExporter(User.class)
                .export(response, users);
    }


}
