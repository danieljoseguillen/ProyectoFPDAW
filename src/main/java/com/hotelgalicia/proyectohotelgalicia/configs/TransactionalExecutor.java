package com.hotelgalicia.proyectohotelgalicia.configs;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionalExecutor {

    @Transactional
    public void run(Runnable r) {
        r.run();
    }

}
