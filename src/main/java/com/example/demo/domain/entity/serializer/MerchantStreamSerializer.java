package com.example.demo.domain.entity.serializer;

import java.io.IOException;

import com.example.demo.domain.entity.Merchant;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;

public class MerchantStreamSerializer implements StreamSerializer<Merchant> {

   @Override
   public void write(ObjectDataOutput out, Merchant merchant) throws IOException {
      out.writeLong(merchant.getId());
      out.writeString(merchant.getMerchantName());
      out.writeString(merchant.getMerchantStreet());
      out.writeString(merchant.getMerchantCity());
   }

   @Override
   public Merchant read(ObjectDataInput in) throws IOException {
      return new Merchant(in.readLong(), in.readString(), in.readString(), in.readString());
   }

   @Override
   public int getTypeId() {
      return 1;
   }
}
