package com.sysgears.service.processor;

import com.sysgears.service.processor.processable.IProcessable;

public interface IProcessableProcessor {
    boolean process(IProcessable processable);
}
