(ns joy-of-clojure.collection-types
  (:require clojure.set))

(def a-to-j
  (vec (map char (range 65 75))))

(a-to-j 4)

(nth a-to-j 4)
(get a-to-j 10)
(a-to-j 10)

(seq a-to-j)

(rseq a-to-j)

(assoc a-to-j 4 "no-longer-e!")

(vec [1 2 3])
(vec `(1 2 3))

`(a-to-j)

(def matrix
  [[1 2 3]
   [4 5 6]
   [7 8 9]])

(get-in matrix [1 2])

(assoc-in matrix [1 2] 'x)

(type 'x)

(update-in matrix [1 2] * 100)

(defn neighbors
  "docstring"
  ([size yx]
   (neighbors [[-1 0] [1 0] [0 -1] [0 1]]
              size
              yx))
  ([deltas size yx]
   (filter (fn [new-yx]
             (every? #(< -1 % size) new-yx))
           (map #(vec (map + yx %)) deltas))))

(neighbors 3 [0 0])

(neighbors 3 [1 1])

(def my-stack [1 2 3])

(peek my-stack)

(pop my-stack)

(conj my-stack 4)

(last my-stack)

(type [])

(subvec a-to-j 3 6)

(def ex-map {:width 10, :height 20, :depth 15})

(first ex-map)
(vector? (first ex-map))

(conj ex-map {:new-entry 13})

(doseq [[dimension amount] ex-map]
  (println (str (name dimension) ":") amount "inches"))

(contains? [1 2 3] "Richard")

(contains? [11 21 31] 1)                                    ; this is a gotcha! Checking contains with a vector returns weird results

(counted? (range 10))

(def a [1 2 3])

(next a)

(pop a)

(pop (quote (1 2 3)))
(pop '(1 2 3))                                              ; another way to make a quote

(contains? (quote (1 2 3)) 1)

(defmethod print-method clojure.lang.PersistentQueue
  [q, w]
  (print-method '<- w)
  (print-method (seq q) w)
  (print-method '-< w))

(print clojure.lang.PersistentQueue/EMPTY)

(def schedule
  (conj clojure.lang.PersistentQueue/EMPTY :wake-up :shower :brush-teeth))

(print schedule)

(count schedule)

(peek schedule)

(pop schedule)

(rest schedule)                                             ; returns a seq, not a queue

; Sets

(#{:a :b :c :d} :c)

(def my-set #{:a :b :c :d})

(contains? my-set :d)

(into my-set #{:e})

(into #{[1 2]} '[(3 2)])

(some #{:b} [:a 1 :b 2])
(some #{:b} [:a 1 :c 2])

(some #{:b} [:a :b])
(some #{:b} [:a :c])                                        ; how to check for containment in a sequece, generally

(symbol 'pizza)

(hash-map :a 1 :b 2 :c 3)
(def my-map (hash-map :a 1 :b 2 :c 3))
(get (hash-map :a 1 :b 2 :c 3) :a)
(:a (hash-map :a 1 :b 2 :c 3))
((hash-map :a 1 :b 2 :c 3) :a)

(seq my-map)
(into {}  (seq my-map))

(apply hash-map [:a 1 :b 2])

(zipmap [:a :b] [1 2])

(sorted-map "rich" 25 "gors" 26)                            ; sorted maps are good for finding and traversing forward or backward through a selection (using subseq or rsubseq)

(assoc {1 :int} 1.0 :float)

(seq my-map)

(seq (array-map :a 1 :b 2 :c 3))