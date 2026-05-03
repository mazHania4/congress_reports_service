package ayd2.ps2026.congress.example.services;

import ayd2.ps2026.congress.example.mappers.ModelMapper;
import ayd2.ps2026.congress.example.repositories.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {

    //private final ModelRepository modelRepository;
    private final ModelMapper modelMapper;

}
