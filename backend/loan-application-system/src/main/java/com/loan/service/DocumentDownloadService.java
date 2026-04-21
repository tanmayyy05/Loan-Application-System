package com.loan.service;

import org.springframework.core.io.Resource;

public interface DocumentDownloadService {

    Resource downloadDocument(Integer documentId);

}
