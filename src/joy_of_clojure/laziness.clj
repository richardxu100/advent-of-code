(ns joy-of-clojure.laziness)

(def baselist (list :barnabas :adam))

(def lst1 (cons :new-entry baselist))
(def lst2 (cons :phoenix baselist))

(identical? (next lst1) (next lst2))

(defn x-conj
  "docstring"
  [node val]
  (cond
    (nil? node)
    {:val val :left nil :right nil}
    (< val (:val node))
    {:val (:val node) :left (x-conj (:left node) val) :right (:right node)}
    :else
    {:val (:val node) :left (:left node) :right (x-conj (:right node) val)}
    ))

(x-conj nil 5)

(x-conj (x-conj {:val 3 :left nil :right nil} 5) 1)

(defn rec-step
  "docstring"
  [[x & xs]]
  (if x
    [x (rec-step xs)]
    []))

(rec-step [1 2 3 4])

(dorun (rec-step (range 10000)))

(def very-lazy (->
                 (iterate #(do (print \.) (inc %)) 1)
                 rest
                 rest
                 rest))

(def less-lazy (->
                 (iterate #(do (print \.) (inc %)) 1)
                 next
                 next
                 next))



(println (first very-lazy))
(println (first less-lazy))

(defn lazy-rec-step
  "docstring"
  [x]
  (lazy-seq (if (seq x)
              [(first x) (lazy-rec-step (rest x))]
              [])))

(lazy-rec-step (range 100))
(class (lazy-rec-step (range 100)))
(dorun (lazy-rec-step (range 100000)))

(defn my-range
  "docstring"
  [i limit]
  (lazy-seq (when (< i limit)
              (cons i (my-range (inc i) limit)))))

(my-range 1 10)

(defn triangle
  "docstring"
  [n]
  (/ (* n (inc n)) 2))

(triangle 10)

(defn lazy-triangle
  []
  (map triangle (iterate inc 1)))

(take 10 (lazy-triangle))

(defn defer-expensive
  "docstring"
  [cheap expensive]
  (if-let [good-enough (force cheap)]
    good-enough
    (force expensive)))

(defer-expensive (delay :cheap)
                 (delay (do (Thread/sleep 5000) :expensive)))

(defer-expensive (delay false)
                 (delay (do (Thread/sleep 5000) :expensive)))

(defn inf-triangles
  "docstring"
  [n]
  {:head (triangle n)
   :tail (delay (inf-triangles (inc n)))})

(force (:tail (inf-triangles 5)))

(defn head
  "docstring"
  [node]
  (:head node))

(defn tail
  "docstring"
  [node]
  (force (:tail node)))

(def tri-nums (inf-triangles 1))

(head tri-nums)

(head (tail tri-nums))

(head (tail (tail (tail tri-nums))))

(defn taker
  "docstring"
  [node n]
  (loop [current node
         remaining n
         result []]
    (if (zero? remaining)
      result
      (recur (tail current) (dec remaining) (conj result (head current))))))

(defn nthr
  "docstring"
  [node n]
  (loop [current node
         remaining n]
    (if (zero? remaining)
      (head current)
      (recur (tail current) (dec remaining)))))

(taker tri-nums 5)

(nthr tri-nums 99)

(defn rand-ints
  "docstring"
  [n]
  (take n (repeatedly #(rand-int n))))

(rand-ints 20)

(defn sort-parts
  "docstring"
  [work]
  (lazy-seq
    (loop [[part & parts] work]
      (if-let [[pivot & xs] (seq part)]
        (let [smaller? #(< % pivot)]
          (recur (list*
                   (filter smaller? xs)
                   pivot
                   (remove smaller? xs)
                   parts)))
        (when-let [[x & parts] parts]
          (cons x (sort-parts parts)))))))

(defn qsort
  "docstring"
  [xs]
  (sort-parts (list xs)))

(qsort (rand-ints 30))
(take 10 (qsort (rand-ints 10000)))










































































