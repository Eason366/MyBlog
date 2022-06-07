import { createStore } from 'vuex'

declare let SessionStorage: any;
const USER = "USER";
const CATEGORY = "CATEGORY";
const TREE = "TREE";

const store = createStore({
  state: {
    user: SessionStorage.get(USER) || {},
    category:SessionStorage.get(CATEGORY) || {},
    tree:SessionStorage.get(TREE) || {},
  },
  mutations: {
    setUser (state, user) {
      state.user = user;
      SessionStorage.set(USER, user);
    },
    setCategory (state, category) {
      state.category = category;
      SessionStorage.set(CATEGORY, category);
    },
    setTree (state, tree) {
      state.tree = tree;
      SessionStorage.set(TREE, tree);
    }
  },
  actions: {
  },
  modules: {
  }
});

export default store;
