import Vue from 'nativescript-vue'
import RadDataForm from 'nativescript-ui-dataform/vue'

import App from './components/App'


Vue.use(RadDataForm)


// Prints Vue logs when --env.production is *NOT* set while building
Vue.config.silent = (TNS_ENV === 'production')


new Vue({
  
  render: h => h('frame', [h(App)])
}).$start()
