import memberLayout from '../components/member/Layout.vue';
import memberModalLayout from '../components/member/modal/Layout.vue';
import Index from '../components/Index.vue';

import memberGuestBookLayout from '../components/member/guestbook/Layout.vue';
const routes =  [
  { path: '/', component: Index, children:[
      // {path: 'login', component: },
      // {path: 'account-recovery', component: ,children:[
      //   {path: 'id', component: , },
      //   {path: 'password', component: , }
      // ]},
      // {path: 'sign-up', component: , },
    ]
  },

  { path: '/member', component: memberLayout, children:[
    {path: 'guestbook', component:memberGuestBookLayout} //test용입니다.
  ]}, 
  { path: '/modal', component: memberModalLayout, children:[
    {path: 'guestbook', component:memberGuestBookLayout}
  ]}


]

export default routes;