/**
 * 
 */
package eu.quanticol.jsstl.examples.bss;

import java.util.Map;

import org.jsstl.core.formula.AndFormula;
import org.jsstl.core.formula.AtomicFormula;
import org.jsstl.core.formula.EventuallyFormula;
import org.jsstl.core.formula.EverywhereFormula;
import org.jsstl.core.formula.GloballyFormula;
import org.jsstl.core.formula.OrFormula;
import org.jsstl.core.formula.ParametricExpression;
import org.jsstl.core.formula.ParametricInterval;
import org.jsstl.core.formula.ReferencedFormula;
import org.jsstl.core.formula.SignalExpression;
import org.jsstl.core.formula.SomewhereFormula;
import org.jsstl.core.formula.jSSTLScript;

/**
 * @author loreti
 *
 */
public class BSSScript extends jSSTLScript {

	public static final double Tf_CONST_ = 40;
	public static final double k_CONST_ = 0;
	public static final double dmax_CONST_ = 11;
	
	public static final int B_VAR_ = 0;
	public static final int S_VAR_ = 1;
	
	public BSSScript() {
		super( 
			new String[] {
				"B",
				"S"
			}
		);	
		addFormula( "phiB" ,
			new AtomicFormula( 
				new ParametricExpression( ) {
				
					public SignalExpression eval( Map<String, Double> parameters ) {
						
						return new SignalExpression() {						
									
							public double eval(double... variables) {
								return ((variables[getIndex(B_VAR_)]) - (k_CONST_));
							}	
												
						};	
									
					}
				
				} , 
				true
			)		
			 ,
			null );
		addFormula( "phiS" ,
			new AtomicFormula( 
				new ParametricExpression( ) {
				
					public SignalExpression eval( Map<String, Double> parameters ) {
						
						return new SignalExpression() {						
									
							public double eval(double... variables) {
								return ((variables[getIndex(S_VAR_)]) - (k_CONST_));
							}	
												
						};	
									
					}
				
				} , 
				true
			)		
			 ,
			null );
		addFormula( "phiTest" ,
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
									return parameters.get("d");
								}
								
							};					
							
						}
						
					} 		
				)		
				 ,
				new ReferencedFormula( 
					this ,
					"phiB"
				)		
			)		
			 ,
			null );
		addFormula( "phid0" ,
			new AndFormula( 
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
										return parameters.get("d");
									}
									
								};					
								
							}
							
						} 		
					)		
					 ,
					new ReferencedFormula( 
						this ,
						"phiB"
					)		
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
										return parameters.get("d");
									}
									
								};					
								
							}
							
						} 		
					)		
					 ,
					new ReferencedFormula( 
						this ,
						"phiS"
					)		
				)		
			)
			 ,
			null );
		addFormula( "phi1" ,
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
									return Tf_CONST_;
								}
								
							};					
							
						}
						
					} 		
				)		
				 ,
				new ReferencedFormula( 
					this ,
					"phid0"
				)		
			)		
			 ,
			null );
		addFormula( "phit0" ,
			new EventuallyFormula( 
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
									return parameters.get("t");
								}
								
							};					
							
						}
						
					} 		
				)		
				 ,
				new AndFormula( 
					new ReferencedFormula( 
						this ,
						"phiB"
					)		
					 ,
					new ReferencedFormula( 
						this ,
						"phiS"
					)		
				)
			)		
			 ,
			null );
		addFormula( "phi2" ,
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
									return 30;
								}
								
							};					
							
						}
						
					} 		
				)		
				 ,
				new ReferencedFormula( 
					this ,
					"phit0"
				)		
			)		
			 ,
			null );
		addFormula( "phi3" ,
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
				new ReferencedFormula( 
					this ,
					"phi1"
				)		
			)		
			 ,
			null );
		addFormula( "phi4" ,
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
				new ReferencedFormula( 
					this ,
					"phi2"
				)		
			)		
			 ,
			null );
		addFormula( "phi5" ,
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
									return 30;
								}
								
							};					
							
						}
						
					} 		
				)		
				 ,
				new OrFormula( 
					new ReferencedFormula( 
						this ,
						"phid0"
					)		
					 ,
					new ReferencedFormula( 
						this ,
						"phit0"
					)		
				)
			)		
			 ,
			null );
		addFormula( "phi6" ,
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
				new ReferencedFormula( 
					this ,
					"phi5"
				)		
			)		
			 ,
			null );
		addParameter( "t" , 
			(double) 0 , 
			(double) 11 );
		addParameter( "d" , 
			(double) 0 , 
			(double) 1.5 );
	}
	
}
