/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.hive.ql.exec.vector.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.ql.exec.Operator;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.plan.OperatorDesc;
import org.apache.hadoop.hive.ql.plan.api.OperatorType;

/**
 * Vectorized data source operator for testing.
 * Used in unit test only.
 *
 */
public class FakeVectorDataSourceOperator extends Operator<FakeVectorDataSourceOperatorDesc>
  implements Serializable {
  private static final long serialVersionUID = 1L;
  private transient Iterable<VectorizedRowBatch> source;

  public static FakeVectorDataSourceOperator addFakeVectorDataSourceParent(
      Iterable<VectorizedRowBatch> source,
      Operator<? extends OperatorDesc> op) {
    FakeVectorDataSourceOperator parent = new FakeVectorDataSourceOperator(source);
    List<Operator<? extends OperatorDesc>> listParents =
        new ArrayList<Operator<? extends OperatorDesc>>(1);
    listParents.add(parent);
    op.setParentOperators(listParents);
    List<Operator<? extends OperatorDesc>> listChildren =
        new ArrayList<Operator<? extends OperatorDesc>>(1);
    listChildren.add(op);
    parent.setChildOperators(listChildren);
    return parent;
  }

  public FakeVectorDataSourceOperator(
    Iterable<VectorizedRowBatch> source) {
    this.source = source;
  }

  @Override
  public Collection<Future<?>> initializeOp(Configuration conf) throws HiveException {
    return super.initializeOp(conf);
  }

  @Override
  public void process(Object row, int tag) throws HiveException {
    for (VectorizedRowBatch unit: source) {
      forward(unit, null);
    }
  }

  @Override
  public OperatorType getType() {
    return null;
  }
}
