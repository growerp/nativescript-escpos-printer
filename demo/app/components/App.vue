<template>
    <Page>
        <ActionBar title="Welcome to NativeScript-ESCPOS-printer!"/>
        <GridLayout columns="*" rows="*">
          <RadDataForm :source="item" :metadata="itemMeta"
            @propertyCommitted="onCommitted"/>
          <Button text="print message" @tap="submit"/>
        </GridLayout>
    </Page>
</template>

<script >
  import { isIOS } from 'tns-core-modules/platform';
  import { Printer } from 'nativescript-escpos-printer'
  export default {
    data() {
      return {
        item: {
          url: isIOS? 'localhost' : '10.0.2.2',
          port: 9100,
          message: "just text you can change",
        },
        itemMeta: {
          propertyAnnotations: [
            { name: 'url', index: 0},
            { name: 'port', index: 1},
            { name: 'message', index: 3}
          ]
        },
        editedItem: null
      }
    },
    methods: {
      onCommitted(data) {
        this.editedItem = JSON.parse(data.object.editedObject)
      },
      submit() {
        var printer = new Printer();
        if (this.editedItem) {
          this.item.ip = this.editedItem.ip
          this.item.port = this.editedItem.port
          this.item.message = this.editedItem.message
        } 
        // add some new lines and cut.....
        let message = this.item.message + '\x0A\x0A\x0A\x0A\x0A\x1D\x56\x42\x00'
        printer.print(this.item.url, this.item.port, message).then(result => {
          if (!result) alert("printer error, start the printer emulator locally?")   
        })
      }
    }
  }
</script>
<style scoped>

</style>