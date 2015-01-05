package com.lahodiuk.bp.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.lahodiuk.bp.Edge;
import com.lahodiuk.bp.Node;
import com.lahodiuk.bp.Potential;

/**
 * Classify chemical compounds, using network of chemical reactions: <br/>
 * Inference of chemical compound types, using network of chemical reactions
 */
public class ChemicalReactionsNetwork {

	public static void main(String[] args) {
		ReactionsNetwork reactionsNetwork = configureReactionsNetwork();

		inference(reactionsNetwork);

		reactionsNetwork.printResults();
	}

	public static void inference(ReactionsNetwork reactionsNetwork) {
		reactionsNetwork.inference(10);
	}

	public static ReactionsNetwork configureReactionsNetwork() {
		ReactionsNetwork reactionsNetwork = new ReactionsNetwork()
				.addRuleWithAllowedPermutations(new Rule().reagentTypes(CompoundType.BASIC_OXIDE, CompoundType.WATER).productTypes(CompoundType.BASE))
				.addRuleWithAllowedPermutations(new Rule().reagentTypes(CompoundType.ACIDIC_OXIDE, CompoundType.WATER).productTypes(CompoundType.ACID))
				.addRuleWithAllowedPermutations(new Rule().reagentTypes(CompoundType.BASIC_OXIDE, CompoundType.ACIDIC_OXIDE).productTypes(CompoundType.SALT))
				.addRuleWithAllowedPermutations(new Rule().reagentTypes(CompoundType.BASE, CompoundType.ACID).productTypes(CompoundType.SALT, CompoundType.WATER))
				.addRuleWithAllowedPermutations(new Rule().reagentTypes(CompoundType.BASE, CompoundType.ACID).productTypes(CompoundType.ACID_SALT, CompoundType.WATER))
				.addRuleWithAllowedPermutations(new Rule().reagentTypes(CompoundType.ACID_SALT, CompoundType.BASE).productTypes(CompoundType.SALT, CompoundType.WATER))
				.addRuleWithAllowedPermutations(new Rule().reagentTypes(CompoundType.BASE, CompoundType.ACIDIC_OXIDE).productTypes(CompoundType.SALT, CompoundType.WATER))
				.addRuleWithAllowedPermutations(new Rule().reagentTypes(CompoundType.BASIC_OXIDE, CompoundType.ACID).productTypes(CompoundType.SALT, CompoundType.WATER))
				.addRuleWithAllowedPermutations(new Rule().reagentTypes(CompoundType.SALT, CompoundType.SALT).productTypes(CompoundType.SALT, CompoundType.SALT))
				// Interesting: it is possible to infer chemical compound types,
				// even without prior knowledge about any of chemical compounds
				// (without "seed")
				// .setPriorCompoundState("Na2O", CompoundType.BASIC_OXIDE)
				.addReaction(new Reaction().reagents("Na2O", "H2O").products("NaOH"))
				.addReaction(new Reaction().reagents("K2O", "H2O").products("KOH"))
				.addReaction(new Reaction().reagents("SO3", "H2O").products("H2SO4"))
				.addReaction(new Reaction().reagents("NaOH", "H2SO4").products("NaHSO4", "H2O"))
				.addReaction(new Reaction().reagents("NaOH", "NaHSO4").products("Na2SO4", "H2O"))
				.addReaction(new Reaction().reagents("NaOH", "H2SO4").products("Na2SO4", "H2O"))
				.addReaction(new Reaction().reagents("KOH", "H2SO4").products("K2SO4", "H2O"))
				.addReaction(new Reaction().reagents("CO2", "H2O").products("H2CO3"))
				.addReaction(new Reaction().reagents("NaOH", "H2CO3").products("NaHCO3", "H2O"))
				.addReaction(new Reaction().reagents("NaOH", "NaHCO3").products("Na2CO3", "H2O"))
				.addReaction(new Reaction().reagents("NaOH", "H2CO3").products("Na2CO3", "H2O"))
				.addReaction(new Reaction().reagents("LiOH", "H2SO4").products("Li2SO4", "H2O"))
				.addReaction(new Reaction().reagents("LiOH", "H2SO4").products("LiHSO4", "H2O"))
				.addReaction(new Reaction().reagents("LiOH", "LiHSO4").products("Li2SO4", "H2O"))
				.addReaction(new Reaction().reagents("Li2O", "H2O").products("LiOH"))
				.addReaction(new Reaction().reagents("KOH", "H2SO4").products("KHSO4", "H2O"))
				.addReaction(new Reaction().reagents("KHSO4", "KOH").products("K2SO4", "H2O"))
				.addReaction(new Reaction().reagents("KOH", "H2CO3").products("K2CO3", "H2O"))
				.addReaction(new Reaction().reagents("KOH", "H2CO3").products("KHCO3", "H2O"))
				.addReaction(new Reaction().reagents("KOH", "KHCO3").products("K2CO3", "H2O"))
				.addReaction(new Reaction().reagents("LiOH", "H2CO3").products("LiHCO3", "H2O"))
				.addReaction(new Reaction().reagents("LiOH", "LiHCO3").products("Li2CO3", "H2O"))
				.addReaction(new Reaction().reagents("Li2O", "CO2").products("Li2CO3"))

				.addReaction(new Reaction().reagents("Al2O3", "H2O").products("Al(OH)3"))
				.addReaction(new Reaction().reagents("Al(OH)3", "H2CO3").products("Al2(CO3)3", "H2O"))
				.addReaction(new Reaction().reagents("Al(OH)3", "KOH").products("KAlO2", "H2O"));

		return reactionsNetwork;
	}

	public enum CompoundType {
		WATER,
		BASIC_OXIDE,
		ACIDIC_OXIDE,
		BASE,
		ACID,
		SALT,
		ACID_SALT
	}

	public static class Rule {
		private List<CompoundType> reagentTypes = new ArrayList<>();
		private List<CompoundType> productTypes = new ArrayList<>();

		public Rule reagentTypes(CompoundType... reagents) {
			for (CompoundType r : reagents) {
				this.reagentTypes.add(r);
			}
			return this;
		}

		public Rule productTypes(CompoundType... products) {
			for (CompoundType p : products) {
				this.productTypes.add(p);
			}
			return this;
		}

		public Rule reagentTypes(List<CompoundType> reagents) {
			for (CompoundType r : reagents) {
				this.reagentTypes.add(r);
			}
			return this;
		}

		public Rule productTypes(List<CompoundType> products) {
			for (CompoundType p : products) {
				this.productTypes.add(p);
			}
			return this;
		}

		public List<CompoundType> getProductTypes() {
			return this.productTypes;
		}

		public List<CompoundType> getReagentTypes() {
			return this.reagentTypes;
		}
	}

	public static class Reaction {
		private List<String> reagents = new ArrayList<>();
		private List<String> products = new ArrayList<>();

		public Reaction reagents(String... reagents) {
			for (String r : reagents) {
				this.reagents.add(r);
			}
			return this;
		}

		public Reaction products(String... products) {
			for (String p : products) {
				this.products.add(p);
			}
			return this;
		}

		public List<String> getProducts() {
			return this.products;
		}

		public List<String> getReagents() {
			return this.reagents;
		}
	}

	public static class ReactionsNetwork {

		private List<Rule> rules = new ArrayList<>();

		private List<Reaction> reactions = new ArrayList<>();

		private Map<Integer, Set<Rule>> reagentsCountToRules = new HashMap<>();

		private Map<Integer, Set<Rule>> productsCountToRules = new HashMap<>();

		private Map<Reaction, ReactionNode> reactionToReactionNode = new HashMap<>();

		private Map<String, CompoundNode> compoundToCompoundNode = new TreeMap<>();

		private List<Edge<?, ?>> edges = new ArrayList<>();

		public ReactionsNetwork addRule(Rule ruleVariant) {
			this.rules.add(ruleVariant);
			return this;
		}

		public ReactionsNetwork addRuleWithAllowedPermutations(Rule rule) {
			List<List<CompoundType>> reagentsPermutations = permutations(rule.getReagentTypes());
			List<List<CompoundType>> productsPermutations = permutations(rule.getProductTypes());

			for (List<CompoundType> reagents : reagentsPermutations) {
				for (List<CompoundType> products : productsPermutations) {
					Rule ruleVariant = new Rule().reagentTypes(reagents).productTypes(products);
					this.addRule(ruleVariant);
				}
			}

			return this;
		}

		public ReactionsNetwork addReaction(Reaction reaction) {
			this.reactions.add(reaction);
			return this;
		}

		public ReactionsNetwork setPriorCompoundState(String compound, CompoundType priorState) {
			this.compoundToCompoundNode.put(compound, new CompoundNode(priorState));
			return this;
		}

		// TODO Consider if needed to use better algorithm
		private static <T> List<List<T>> permutations(List<T> seq) {
			if (seq.size() == 1) {
				return Arrays.asList(seq);
			}
			List<List<T>> perm = permutations(seq.subList(1, seq.size()));

			List<List<T>> result = new ArrayList<>();
			for (List<T> p : perm) {
				for (int i = 0; i < p.size(); i++) {
					List<T> r = new LinkedList<>();
					r.addAll(p.subList(0, i));
					r.add(seq.get(0));
					r.addAll(p.subList(i, p.size()));
					result.add(r);
				}
				List<T> r = new LinkedList<>();
				r.addAll(p);
				r.add(seq.get(0));
				result.add(r);
			}
			return result;
		}

		private void buildNetwork() {

			this.processRules();

			this.processReactions();

			for (Reaction reaction : this.reactionToReactionNode.keySet()) {
				ReactionNode reactionNode = this.reactionToReactionNode.get(reaction);

				List<String> reagents = reaction.getReagents();

				for (int position = 0; position < reagents.size(); position++) {
					String reagent = reagents.get(position);
					CompoundNode reagentNode = this.compoundToCompoundNode.get(reagent);
					this.edges.add(Edge.connect(reagentNode, reactionNode, new ReagentReactionCompatibilityPotential(position)));
				}

				List<String> products = reaction.getProducts();

				for (int position = 0; position < products.size(); position++) {
					String product = products.get(position);
					CompoundNode productNode = this.compoundToCompoundNode.get(product);
					this.edges.add(Edge.connect(productNode, reactionNode, new ProductReactionCompatibilityPotential(position)));
				}
			}
		}

		private void processRules() {
			for (Rule rule : this.rules) {
				int reagentsCount = rule.getReagentTypes().size();
				Set<Rule> rulesWithSameNumberOfReagents = this.reagentsCountToRules.get(reagentsCount);
				if (rulesWithSameNumberOfReagents == null) {
					rulesWithSameNumberOfReagents = new HashSet<>();
				}
				rulesWithSameNumberOfReagents.add(rule);
				this.reagentsCountToRules.put(reagentsCount, rulesWithSameNumberOfReagents);

				int productsCount = rule.getProductTypes().size();
				Set<Rule> rulesWithSameNumberOfProducts = this.productsCountToRules.get(productsCount);
				if (rulesWithSameNumberOfProducts == null) {
					rulesWithSameNumberOfProducts = new HashSet<>();
				}
				rulesWithSameNumberOfProducts.add(rule);
				this.productsCountToRules.put(productsCount, rulesWithSameNumberOfProducts);
			}
		}

		public void processReactions() {
			for (Reaction reaction : this.reactions) {
				int reagentsCount = reaction.getReagents().size();
				int productsCount = reaction.getProducts().size();

				Set<Rule> mostProbableRules = this.getMostProbableRulesByReagentsAndProductsCount(reagentsCount, productsCount);

				this.reactionToReactionNode.put(reaction, new ReactionNode(mostProbableRules));

				for (String reagent : reaction.getReagents()) {
					if (this.compoundToCompoundNode.get(reagent) == null) {
						this.compoundToCompoundNode.put(reagent, new CompoundNode());
					}
				}

				for (String product : reaction.getProducts()) {
					if (this.compoundToCompoundNode.get(product) == null) {
						this.compoundToCompoundNode.put(product, new CompoundNode());
					}
				}
			}
		}

		private Set<Rule> getMostProbableRulesByReagentsAndProductsCount(int reagentsCount, int productsCount) {
			Set<Rule> mostProbableStatesByReagentsCount = this.reagentsCountToRules.get(reagentsCount);
			Set<Rule> mostProbableStatesByProductsCount = this.productsCountToRules.get(productsCount);

			Set<Rule> mostProbableRules = new HashSet<>();

			// Intersection of sets
			if ((mostProbableStatesByProductsCount != null) && (mostProbableStatesByReagentsCount != null)) {
				for (Rule s : mostProbableStatesByProductsCount) {
					if (mostProbableStatesByReagentsCount.contains(s)) {
						mostProbableRules.add(s);
					}
				}
			}

			if (mostProbableRules.isEmpty()) {
				throw new RuntimeException("Can't find any reactions withs given numbers of products and reagents");
			}

			return mostProbableRules;
		}

		public ReactionsNetwork inference(int times) {
			this.buildNetwork();

			for (int i = 0; i < times; i++) {
				for (Edge<?, ?> edge : this.edges) {
					edge.updateMessagesNode1ToNode2();
				}
				for (Edge<?, ?> edge : this.edges) {
					edge.refreshMessagesNode1ToNode2();
				}
				for (Edge<?, ?> edge : this.edges) {
					edge.updateMessagesNode2ToNode1();
				}
				for (Edge<?, ?> edge : this.edges) {
					edge.refreshMessagesNode2ToNode1();
				}
			}
			return this;
		}

		public void printResults() {
			for (String compound : this.compoundToCompoundNode.keySet()) {
				System.out.println(compound + "\t" + this.compoundToCompoundNode.get(compound).getPosteriorProbabilities());
				System.out.println(compound + "\t" + this.compoundToCompoundNode.get(compound).getMostProbableState());
				System.out.println();
			}
		}

		public CompoundType getMostProbableCompoundType(String compound) {
			return this.compoundToCompoundNode.get(compound).getMostProbableState();
		}
	}

	private static class CompoundNode extends Node<CompoundType> {

		private static final double EPSILON = 1e-5;

		private static final List<CompoundType> STATES = Arrays.asList(CompoundType.values());

		private CompoundType mostProbableState;

		public CompoundNode() {
		}

		public CompoundNode(CompoundType mostProbableState) {
			this.mostProbableState = mostProbableState;
		}

		@Override
		public Iterable<CompoundType> getStates() {
			return STATES;
		}

		@Override
		public double getPriorProbablility(CompoundType state) {
			if (this.mostProbableState == null) {
				return 1.0 / STATES.size();
			} else {
				if (state == this.mostProbableState) {
					return 1.0 - EPSILON;
				} else {
					return EPSILON;
				}
			}
		}
	}

	private static class ReactionNode extends Node<Rule> {

		private static final double EPSILON = 1e-5;

		private Set<Rule> mostProbableStates = null;

		public ReactionNode(Set<Rule> mostProbableStates) {
			this.mostProbableStates = mostProbableStates;
		}

		@Override
		public Iterable<Rule> getStates() {
			return this.mostProbableStates;
		}

		@Override
		public double getPriorProbablility(Rule state) {
			if (this.mostProbableStates.contains(state)) {
				return 1.0 / this.mostProbableStates.size();
			} else {
				return EPSILON;
			}
		}
	}

	private static class ReagentReactionCompatibilityPotential extends Potential<CompoundType, Rule> {

		private static final double EPSILON = 1e-5;

		private final int position;

		public ReagentReactionCompatibilityPotential(int position) {
			this.position = position;
		}

		@Override
		public double getValue(CompoundType compoundState, Rule reactionState) {
			if (reactionState.getReagentTypes().get(this.position) == compoundState) {
				return 1 - EPSILON;
			} else {
				return EPSILON;
			}
		}
	}

	private static class ProductReactionCompatibilityPotential extends Potential<CompoundType, Rule> {

		private static final double EPSILON = 1e-5;

		private final int position;

		public ProductReactionCompatibilityPotential(int position) {
			this.position = position;
		}

		@Override
		public double getValue(CompoundType compoundState, Rule reactionState) {
			if (reactionState.getProductTypes().get(this.position) == compoundState) {
				return 1 - EPSILON;
			} else {
				return EPSILON;
			}
		}
	}
}
