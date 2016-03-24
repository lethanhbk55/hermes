package com.gaia.hermes.processor;

import com.nhb.common.data.PuElement;
import com.nhb.common.data.PuObject;

public interface HermesProcessor {

	PuElement execute(PuObject message);
}
