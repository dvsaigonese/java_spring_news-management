package com.GoalLineNews.service;

import com.GoalLineNews.dto.TagDTO;
import com.GoalLineNews.entity.Tag;
import com.GoalLineNews.repository.TagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<TagDTO> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    public TagDTO getTagById(int id) {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        return tagOptional.map(this::convertEntityToDTO).orElse(null);
    }

    public TagDTO createTag(TagDTO tagDTO) {
        Tag tag = convertDTOToEntity(tagDTO);
        Tag savedTag = tagRepository.save(tag);
        return convertEntityToDTO(savedTag);
    }

    public TagDTO updateTag(int id, TagDTO tagDTO) {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        if (tagOptional.isPresent()) {
            Tag tag = tagOptional.get();
            tag.setName(tagDTO.getName());
            // Update other fields as needed

            Tag updatedTag = tagRepository.save(tag);
            return convertEntityToDTO(updatedTag);
        }
        return null;
    }

    public void deleteTag(int id) {
        tagRepository.deleteById(id);
    }


    public List<TagDTO> getTagsByNewsId(int newsId) {
        List<Integer> tagIds = tagRepository.findTagIdsByNewsId(newsId);
        List<Tag> tags = tagRepository.findAllById(tagIds);
        return tags.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    private TagDTO convertEntityToDTO(Tag tag) {
        return modelMapper.map(tag, TagDTO.class);
    }

    private Tag convertDTOToEntity(TagDTO tagDTO) {
        return modelMapper.map(tagDTO, Tag.class);
    }
}
