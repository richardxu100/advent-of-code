(ns exercises.destructuring)

(def user {:name "something"
           :age 30
           :email "email@example.com"})

(let [user-name (:name user)
      user-email (:email user)]
  [user-email user-name])

(let [{:keys [name email] :as current-user} user]
  [name email]
  current-user)

(defn do-something
  "docstring"
  [{:keys [my-config-option]
   :as opts}]
  (call-other-fn! my-config-option)
  (some-other-fn! opts))

(let [{:keys [name email non-existing-key]
       :or {name "default name"
            non-existing-key 1}}
        user]
  [name email non-existing-key])

(defn my-function
  "docstring"
  [{:keys [my-first-property ..]}]
  )