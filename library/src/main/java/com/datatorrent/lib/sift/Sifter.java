/*
 * Copyright (c) 2014 DataTorrent, Inc. ALL Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datatorrent.lib.sift;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.base.Predicate;

import com.datatorrent.api.BaseOperator;
import com.datatorrent.api.DefaultInputPort;
import com.datatorrent.api.DefaultOutputPort;

/**
 * Drops events which satisfy any predicate of a list of predicates.
 *
 * @param <INPUT> type of input event.
 */
public class Sifter<INPUT> extends BaseOperator
{
  @Nonnull
  private List<Predicate<INPUT>> predicates;

  /**
   * Sets the list of {@link Predicate}s against which each tuple is checked.
   *
   * @param predicates  predicate list
   */
  public void setPredicates(@Nonnull List<Predicate<INPUT>> predicates)
  {
    this.predicates = predicates;
  }

  public final transient DefaultOutputPort<INPUT> output = new DefaultOutputPort<INPUT>();
  public final transient DefaultInputPort<INPUT> input = new DefaultInputPort<INPUT>()
  {

    @Override
    public void process(INPUT input)
    {
      for (Predicate<INPUT> predicate : predicates) {
        if (predicate.apply(input)) {
          return;
        }
      }
      output.emit(input);
    }
  };

}
