package com.adkan.adkan.board;

import com.adkan.adkan.common.ServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public class BoardService implements ServiceInterface<Board> {
    /** Service Class
     * A Service class helps to contain all entity logic in a single class
     */
    @Autowired
    private BoardRepository boardRepository;
    @Override
    public List<Board> getAll() {
        return (List<Board>) boardRepository.findAll();
    }

    @Override
    public Optional<Board> getById(int id) {
        return boardRepository.findById(id);
    }

    @Override
    public Board save(Board entity) {
        return boardRepository.save(entity);
    }

    @Override
    public Optional<Board> update(Board entity) {
        Optional<Board> updatedEntity = Optional.empty();
        updatedEntity = boardRepository.findById(entity.getId());
        if (!updatedEntity.isEmpty())
            boardRepository.save(entity);
        return updatedEntity;
    }

    @Override
    public Optional<Board> partialUpdate(Integer id, Map<Object, Object> fields) {
        try {
            Board entity = getById(id).get();
            if (entity == null) {
                return Optional.empty();
            }
            Optional<Board> updatedEntity = Optional.empty();
            // Map key is field name, v is value
            fields.forEach((updatedField, value) -> {
                // use reflection to get fields updatedField on manager and set it to value updatedField
                Field field = ReflectionUtils.findField(Board.class, (String) updatedField);
                field.setAccessible(true);
                ReflectionUtils.setField(field, entity, value);
            });
            boardRepository.save(entity);
            updatedEntity = Optional.of(entity);
            return updatedEntity;
        } catch (Exception exception) {
            System.err.println(exception);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Board> delete(int id) {
        Optional<Board> entity = Optional.empty();
        entity = getById(id).map(e -> {
            boardRepository.delete(e);
            return e;
        });
        return entity;

    }
}
