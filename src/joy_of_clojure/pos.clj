(ns joy-of-clojure.pos)

(defn pos
  "docstring"
  [e coll]
  (let [cmp (if (map? coll)
              #(= (second %1) %2)
              #(= %1 %2))]
    (loop [s coll idx 0]
      (when (seq s)
        (if (cmp (first s) e)
          (if (map? coll)
            (first (first s))
            idx)
          (recur (next s) (inc idx)))))
    ))

(pos 3 [:a 1 :b 2 :c 3 :d 4])

(pos :foo [:a 1 :b 2 :c 3 :d 4])

(pos 3 {:a 1 :b 2 :c 3 :d 4})

(pos \3 ":a 1 : b2 :c 3 :d 4")

(def my-compare
  #(= %1 %2))

(my-compare 2 3)
(my-compare 2 2)

(def my-compare-for-map
  #(= (second %1) %2))

(my-compare-for-map [:a "rich"]  "rich")

(defn index
  "docstring"
  [coll]
  (cond
    (map? coll) (seq coll)
    (set? coll) (map vector coll coll)
    :else (map vector (iterate inc 0) coll)))

(index ["rich" "gors" "another"])

(defn new-pos
  "docstring"
  [e coll]
  (let [indexed-coll (index coll)]
    (map first (filter (fn [pair] (= (second pair) e)) indexed-coll)) )
  )

(new-pos 3 [:a 1 :b 2 :c 3 :d 4])

(new-pos :foo [:a 1 :b 2 :c 3 :d 4])
(new-pos 3 {:a 1 :b 2 :c 3 :d 4})
(new-pos 3 {:a 3 :b 3 :c 3 :d 4})

(defn new-pos-from-book
  "docstring"
  [e coll]
  (for [[i v] (index coll) :when (= e v)] i))

(new-pos-from-book 3 {:a 3 :b 3 :c 3 :d 4})

(defn pos-with-pred
  "docstring"
  [pred coll]
  (for [[i v] (index coll) :when (pred v)] i))

(pos-with-pred even? [1 2 3 4 5])

(pos-with-pred #{3 4} {:a 3 :b 3 :c 3 :d 4})

(index #{:a :b :c :d})

(index [:a :b :c :d])