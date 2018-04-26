/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package beibei.search.test;

import java.io.IOException;

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.IntsRef;
import org.apache.lucene.util.IntsRefBuilder;
import org.apache.lucene.util.fst.Builder;
import org.apache.lucene.util.fst.ByteSequenceOutputs;
import org.apache.lucene.util.fst.FST;
import org.apache.lucene.util.fst.Outputs;
import org.apache.lucene.util.fst.Util;

public class FstTest {
  
  public static void main(String[] args) throws IOException {
    // 输入输出
    String[] inputTerms = {"02", "34", "12", "33", "中国人"};
    byte[][] outputValues = {{1}, {2}, {3}, {4}, {5}};
    // fst构造器
    Outputs<BytesRef> outputs = ByteSequenceOutputs.getSingleton();
    final Builder<BytesRef> indexBuilder = new Builder<>(FST.INPUT_TYPE.BYTE1, outputs);
    // 构造
    BytesRef inputBytes = new BytesRef();
    IntsRefBuilder inputInts = new IntsRefBuilder();
    for (int i = 0; i < inputTerms.length; i++) {
      inputBytes = new BytesRef(inputTerms[i]);
      indexBuilder.add(Util.toIntsRef(inputBytes, inputInts), new BytesRef(outputValues[i]));
    }
    FST<BytesRef> fst = indexBuilder.finish();
    System.out.println(fst.getFirstArc(new FST.Arc<BytesRef>()).output);
    FST.Arc<BytesRef> lastFst = fst.readLastTargetArc(fst.getFirstArc(new FST.Arc<BytesRef>()), new FST.Arc<BytesRef>(), fst.getBytesReader());
   
    lastFst=fst.findTargetArc(1, fst.getFirstArc(new FST.Arc<BytesRef>()), new FST.Arc<BytesRef>(), fst.getBytesReader());
//    System.out.println(lastFst.output);
    for (int i = 0; i < inputTerms.length; i++) {
      //看get源码，有对findTargetArc的使用
      BytesRef value = Util.get(fst, new BytesRef(inputTerms[i]));
      System.out.println(value);
    }
    
  }
}
