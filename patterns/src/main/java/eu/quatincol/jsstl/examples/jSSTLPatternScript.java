/**
 * 
 */
package eu.quatincol.jsstl.examples;

import eu.quanticol.jsstl.core.formula.jSSTLScript;
import eu.quanticol.jsstl.core.formula.*;
import java.util.Map;		

/**
 * @author loreti
 *
 */
public class jSSTLPatternScript extends jSSTLScript {

		public static final double h_CONST_ = 0.5;
		public static final double d1_CONST_ = 1;
		public static final double d2_CONST_ = 6;
		public static final double Tp_CONST_ = 38;
		public static final double delta_CONST_ = 2;
		public static final double Tend_CONST_ = 50;
		public static final double dmax_CONST_ = 64;
		public static final double dspot_CONST_ = 10;
		public static final double hpert_CONST_ = 10;
		
		public static final int xA_VAR_ = 0;
		public static final int xB_VAR_ = 1;
		
		public jSSTLPatternScript() {
			super( 
				new String[] {
					"xA",
					"xB"
				}
			);	
			addFormula( "spot" ,
				new SurroundFormula( 
					new ParametricInterval( 
						new ParametricExpression() {
						
							public SignalExpression eval( final Map<String,Double> parameters ) {
					
								return new SignalExpression() {
									
									public double eval( double ... variables ) {
										return d1_CONST_;
									}
									
								};					
								
							}
							
						} , 
						new ParametricExpression() {
						
							public SignalExpression eval( final Map<String,Double> parameters ) {
					
								return new SignalExpression() {
									
									public double eval( double ... variables ) {
										return d2_CONST_;
									}
									
								};					
								
							}
							
						} 		
					)		
					 ,
					new AtomicFormula( 
						new ParametricExpression( ) {
						
							public SignalExpression eval( Map<String, Double> parameters ) {
								
								return new SignalExpression() {						
											
									public double eval(double... variables) {
										return ((h_CONST_) - (variables[getIndex(xA_VAR_)]));
									}	
														
								};	
											
							}
						
						} , 
						true
					)		
					 ,
					new AtomicFormula( 
						new ParametricExpression( ) {
						
							public SignalExpression eval( Map<String, Double> parameters ) {
								
								return new SignalExpression() {						
											
									public double eval(double... variables) {
										return ((variables[getIndex(xA_VAR_)]) - (h_CONST_));
									}	
														
								};	
											
							}
						
						} , 
						false
					)		
				)		
				 ,
				null );
			addFormula( "spotformation" ,
				new EventuallyFormula( 
					new ParametricInterval( 
						new ParametricExpression() {
						
							public SignalExpression eval( final Map<String,Double> parameters ) {
					
								return new SignalExpression() {
									
									public double eval( double ... variables ) {
										return Tp_CONST_;
									}
									
								};					
								
							}
							
						} , 
						new ParametricExpression() {
						
							public SignalExpression eval( final Map<String,Double> parameters ) {
					
								return new SignalExpression() {
									
									public double eval( double ... variables ) {
										return ( Tp_CONST_ )+( delta_CONST_ );
									}
									
								};					
								
							}
							
						} 		
					)		
					 ,
					new GloballyFormula( 
						new ParametricInterval( 
							new ParametricExpression() {
							
								public SignalExpression eval( final Map<String,Double> parameters ) {
						
									return new SignalExpression() {
										
										public double eval( double ... variables ) {
											return 0;
										}
										
									};					
									
								}
								
							} , 
							new ParametricExpression() {
							
								public SignalExpression eval( final Map<String,Double> parameters ) {
						
									return new SignalExpression() {
										
										public double eval( double ... variables ) {
											return Tend_CONST_;
										}
										
									};					
									
								}
								
							} 		
						)		
						 ,
						new ReferencedFormula( 
							this ,
							"spot"
						)		
					)		
				)		
				 ,
				null );
			addFormula( "pattern" ,
				new EverywhereFormula( 
					new ParametricInterval( 
						new ParametricExpression() {
						
							public SignalExpression eval( final Map<String,Double> parameters ) {
					
								return new SignalExpression() {
									
									public double eval( double ... variables ) {
										return 0;
									}
									
								};					
								
							}
							
						} , 
						new ParametricExpression() {
						
							public SignalExpression eval( final Map<String,Double> parameters ) {
					
								return new SignalExpression() {
									
									public double eval( double ... variables ) {
										return dmax_CONST_;
									}
									
								};					
								
							}
							
						} 		
					)		
					 ,
					new SomewhereFormula( 
						new ParametricInterval( 
							new ParametricExpression() {
							
								public SignalExpression eval( final Map<String,Double> parameters ) {
						
									return new SignalExpression() {
										
										public double eval( double ... variables ) {
											return 0;
										}
										
									};					
									
								}
								
							} , 
							new ParametricExpression() {
							
								public SignalExpression eval( final Map<String,Double> parameters ) {
						
									return new SignalExpression() {
										
										public double eval( double ... variables ) {
											return dspot_CONST_;
										}
										
									};					
									
								}
								
							} 		
						)		
						 ,
						new ReferencedFormula( 
							this ,
							"spotformation"
						)		
					)		
				)		
				 ,
				null );
			addFormula( "perturbation" ,
				new AndFormula( 
					new AtomicFormula( 
						new ParametricExpression( ) {
						
							public SignalExpression eval( Map<String, Double> parameters ) {
								
								return new SignalExpression() {						
											
									public double eval(double... variables) {
										return ((variables[getIndex(xA_VAR_)]) - (hpert_CONST_));
									}	
														
								};	
											
							}
						
						} , 
						false
					)		
					 ,
					new ReferencedFormula( 
						this ,
						"spot"
					)		
				)
				 ,
				null );
		}

	}
