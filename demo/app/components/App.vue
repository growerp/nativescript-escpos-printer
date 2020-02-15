<template>
    <Page>
        <ActionBar title="Welcome to nativescript-escpos-printer!"/>
        <GridLayout columns="20,*,20" rows="*">
          <StackLayout column="1">
            <RadDataForm :source="item"  @propertyCommitted="onItemCommitted"/>
            <Button text="print" @tap="print(item)" width="50%"/>
          </StackLayout>
        </GridLayout>
    </Page>
</template>

<script lang="ts">
  import { Client } from 'nativescript-escpos-printer';
  import { isIOS, isAndroid } from 'tns-core-modules/platform';
  export default {
    data() {
      return {
        item: {servername: isIOS? 'localhost': '10.0.2.2', port: 9100, message: 'send this to printer'},
        editedItem: null,
      }
    },
    methods: {
      onItemCommitted(data) {
        this.editedItem = JSON.parse(data.object.editedObject)
      },
      print() {
        if (!this.editedItem) this.editedItem = this.item
        var client = new Client
        console.log("printing with data: " + JSON.stringify(this.editedItem))
        if (!client.print(this.editedItem.servername,
              this.editedItem.port, this.editedItem.message)) 
          alert("printer error...")
      }
    }
  }
</script>

<style scoped>
    ActionBar {
        background-color: #53ba82;
        color: #ffffff;
    }

    .message {
        vertical-align: center;
        text-align: center;
        font-size: 20;
        color: #333333;
    }
</style>
