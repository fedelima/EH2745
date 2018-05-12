package assignment1;

import java.util.ArrayList;

public class SearchRoutines {
	
	//*** LINE SEARCH ALGORITHM ***
	public static void line_search(Double SB, 
			ArrayList<ACLineSegment> line_list,
			ArrayList<Terminal> terminal_list,
			ArrayList<ConnectivityNode> cnode_list,
			ArrayList<Breaker> breaker_list,
			ArrayList<BusbarSection> busbar_list,
			ArrayList<VoltageLevel> voltlvl_list,
			ArrayList<BaseVoltage> basevolt_list,
			ArrayList<Ybus> ybus_list) {
		
		//Search algorithm to connect lines to buses:
		//ACLineSegment => Terminal => CNode => Terminal => Breaker => Busbar (through equipment container)		
		String []  To = new String[cnode_list.size()];
		String []  From = new String[cnode_list.size()];
		//ArrayList<String> From = new ArrayList<String>();
		//ArrayList<String> To = new ArrayList<String>();
		String devType = null, dev = null;
		Double R = null, X= null, G= null, B= null;
		Double VB = null; //Voltage base (kV).
		Double ZB = null; //Impedance base (ohm).
		
		boolean notfound ; //From-To flag.
		int term = 1; //
		int multiL = 0;
		int multiR = 0;
		Terminal leftT= new Terminal();;
		Terminal rightT = new Terminal();
		
		for (ACLineSegment line : line_list) {
			for (Terminal terminal1 : terminal_list) {
				if (line.id.equals(terminal1.ConductingEquipment)) {
					if (term == 1) {
						leftT = terminal1;
						term = 2;
					}
					for (ConnectivityNode cnode : cnode_list) {
							if (cnode.id.equals(leftT.ConnectivityNode)) {
								for (Terminal terminal2 : terminal_list) {
									if (!terminal2.id.equals(leftT.id) && cnode.id.equals(terminal2.ConnectivityNode)) {
										for (Breaker breaker : breaker_list) {
											if (breaker.id.equals(terminal2.ConductingEquipment)) {
												for (BusbarSection busbar : busbar_list) {
													if (busbar.EquipmentContainer.equals(breaker.EquipmentContainer)) {
														if (breaker.open.equals("false")){																
																From[multiL] = busbar.name; //From bus.											
																VB = busbar.getBaseVoltage(voltlvl_list,basevolt_list); //Get base voltage of bus.
																multiL+=1;
														}
													}
												}
											}
										}
									}
								}		
						}
					}
				}
			}	
			for (Terminal terminal1 : terminal_list) {
				if (line.id.equals(terminal1.ConductingEquipment) && !leftT.id.equals(terminal1.id)) {
					if (term==2) {
						rightT = terminal1;
						term = 3;
					}
					for (ConnectivityNode cnode : cnode_list) {
							if (cnode.id.equals(rightT.ConnectivityNode)) {
								for (Terminal terminal2 : terminal_list) {
									if (!terminal2.id.equals(rightT.id) && cnode.id.equals(terminal2.ConnectivityNode)) {
										for (Breaker breaker : breaker_list) {
											if (breaker.id.equals(terminal2.ConductingEquipment)) {
												for (BusbarSection busbar : busbar_list) {
													if (busbar.EquipmentContainer.equals(breaker.EquipmentContainer)) {
														if (breaker.open.equals("false")){
																To[multiR] = busbar.name; //To bus.	
																multiR+=1;	
														}
													}
												}
											}
										}
									}
								}		
						}
					}
				}
			}
			ZB = Math.pow(VB,2)/SB; //Calculate base impedance at node.														
			R = line.rtot/ZB; //Per unit resistance.
			X = line.xtot/ZB; //Per unit reactance.															
			G = line.gtot*ZB; //Per unit admittance.
			B = line.btot*ZB; //Per unit susceptance.
			devType ="Line";
			dev = line.name;
			for (int temp_i=0; temp_i<multiL; temp_i++) {
				notfound = true;
				for (int temp_j=0; temp_j<multiR; temp_j++) {
					for (Ybus ybus_temp : ybus_list) {
						if ((ybus_temp.From.equals(From[temp_i])) && (ybus_temp.To.equals(To[temp_j])) && (ybus_temp.dev.equals(dev))){
							notfound = false;
						}						
					}
					if (notfound) {
						ybus_list.add(new Ybus(From[temp_i],To[temp_j],R,X,G,B,devType,dev)); //Add branch to Y-Bus.
					}	
				}	
			}
			multiL=0;
			multiR=0;
			term=1;
		}	
	}
	
	//*** TRANSFORMER SEARCH ALGORITHM ***
	public static void trafo_search(Double SB, 
			ArrayList<PowerTransformer> trafo_list,
			ArrayList<PowerTransformerEnd> trafoEnd_list,
			ArrayList<Terminal> terminal_list,
			ArrayList<ConnectivityNode> cnode_list,
			ArrayList<Breaker> breaker_list,
			ArrayList<BusbarSection> busbar_list,
			ArrayList<VoltageLevel> voltlvl_list,
			ArrayList<BaseVoltage> basevolt_list,
			ArrayList<Ybus> ybus_list) {
		
		//Search algorithm to connect transformers to buses:
		//TransformerEnd => Terminal => CNode => Terminal => Breaker => Busbar (through equipment container)
		String []  To = new String[cnode_list.size()];
		String []  From = new String[cnode_list.size()];
		Double R = null, X = null;
		Double VB = null; //Voltage base (kV).
		Double ZB = null; //Impedance base (ohm).
		Double VBn = null; //nominal Voltage base (kV).
		Double ZBn = null; //nominal Impedance base (ohm).
		Double SBn = null; //nominal power base
		String dev, devType; 
		
		boolean notfound ; //From-To flag.
		int term = 1; //
		int multiL = 0;
		int multiR = 0;
		Terminal leftT= new Terminal();;
		Terminal rightT = new Terminal();
		
		for (PowerTransformer trafo : trafo_list) {
			R=0.0;
			X=0.0;
			for (PowerTransformerEnd trafoEnd : trafoEnd_list) {
				for (Terminal terminal1 : terminal_list) {
					if (trafo.id.equals(terminal1.ConductingEquipment)) {
						if (trafoEnd.terminal_id.equals(terminal1.id)) {
							if (term == 1) {
								leftT = terminal1;
								term = 2;
							}
							for (ConnectivityNode cnode : cnode_list) {
								if (cnode.id.equals(leftT.ConnectivityNode)) {
									for (Terminal terminal2 : terminal_list) {
										if (!terminal2.id.equals(leftT.id) && cnode.id.equals(terminal2.ConnectivityNode)) {
											for (BusbarSection busbar : busbar_list) {
												if (busbar.id.equals(terminal2.ConductingEquipment) || busbar.id.equals(terminal1.ConductingEquipment)) {
													From[multiL] = busbar.name; //From bus.											
													VB = busbar.getBaseVoltage(voltlvl_list,basevolt_list); //Get base voltage of bus.
													ZB = Math.pow(VB,2)/SB; //Calculate base impedance at node.
													VBn = trafoEnd.VBn; //Get nominal base voltage of the Transformer.
													SBn = trafoEnd.SBn; //Get nominal base power of the Transformer.
													ZBn = Math.pow(VBn,2)/SBn; //Calculate base impedance at node.												
													R += trafoEnd.rtot/ZB; //Per unit resistance. *ZBn
													X += trafoEnd.xtot/ZB; //Per unit reactance.	*ZBn
													devType ="Transformer";
													dev = trafoEnd.name;
													multiL+=1;
																	
												}
											}
											for (Breaker breaker : breaker_list) {
												if (breaker.id.equals(terminal2.ConductingEquipment)) {
													for (BusbarSection busbar : busbar_list) {
														if (busbar.EquipmentContainer.equals(breaker.EquipmentContainer)) {
															if (breaker.open.equals("false")){
																System.out.println(multiL);
																From[multiL] = busbar.name; //From bus.											
																VB = busbar.getBaseVoltage(voltlvl_list,basevolt_list); //Get base voltage of bus.
																ZB = Math.pow(VB,2)/SB; //Calculate base impedance at node.
																VBn = trafoEnd.VBn; //Get nominal base voltage of the Transformer.
																SBn = trafoEnd.SBn; //Get nominal base power of the Transformer.
																ZBn = Math.pow(VBn,2)/SBn; //Calculate base impedance at node.												
																R += trafoEnd.rtot/ZB; //Per unit resistance. *ZBn
																X += trafoEnd.xtot/ZB; //Per unit reactance.	*ZBn
																devType ="Transformer";
																dev = trafoEnd.name;
																multiL+=1;
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}						
					}
				}
				for (Terminal terminal1 : terminal_list) {
					if (trafo.id.equals(terminal1.ConductingEquipment) && !leftT.id.equals(terminal1.id)) {
						if (trafoEnd.terminal_id.equals(terminal1.id)) {
							if (term==2) {
								rightT = terminal1;
								term = 3;
							}
							for (ConnectivityNode cnode : cnode_list) {
								if (cnode.id.equals(rightT.ConnectivityNode)) {
									for (Terminal terminal2 : terminal_list) {
										if (!terminal2.id.equals(rightT.id) && cnode.id.equals(terminal2.ConnectivityNode)) {
											
											for (BusbarSection busbar : busbar_list) {
												if (busbar.id.equals(terminal2.ConductingEquipment) || busbar.id.equals(terminal1.ConductingEquipment)) {
													To[multiR] = busbar.name; //From bus.
													if (term == 3) {																								
														VB = busbar.getBaseVoltage(voltlvl_list,basevolt_list); //Get base voltage of bus.
														ZB = Math.pow(VB,2)/SB; //Calculate base impedance at node.
														VBn = trafoEnd.VBn; //Get nominal base voltage of the Transformer.
														SBn = trafoEnd.SBn; //Get nominal base power of the Transformer.
														ZBn = Math.pow(VBn,2)/SBn; //Calculate base impedance at node.
														//From[] = busbar.name; //From bus.
														R += trafoEnd.rtot/ZB; //Per unit resistance. *ZBn
														X += trafoEnd.xtot/ZB; //Per unit reactance.	*ZBn
														devType ="Transformer";
														dev = trafoEnd.name;
														term = 4;
													}
													multiR+=1;
												}
											}
											for (Breaker breaker : breaker_list) {
												if (breaker.id.equals(terminal2.ConductingEquipment)) {
													for (BusbarSection busbar : busbar_list) {
														if (busbar.EquipmentContainer.equals(breaker.EquipmentContainer)) {
															if (breaker.open.equals("false")){
																To[multiR] = busbar.name; //From bus.
																if (term == 3) {																								
																	VB = busbar.getBaseVoltage(voltlvl_list,basevolt_list); //Get base voltage of bus.
																	ZB = Math.pow(VB,2)/SB; //Calculate base impedance at node.
																	VBn = trafoEnd.VBn; //Get nominal base voltage of the Transformer.
																	SBn = trafoEnd.SBn; //Get nominal base power of the Transformer.
																	ZBn = Math.pow(VBn,2)/SBn; //Calculate base impedance at node.
																	//From[] = busbar.name; //From bus.
																	R += trafoEnd.rtot/ZB; //Per unit resistance. *ZBn
																	X += trafoEnd.xtot/ZB; //Per unit reactance.	*ZBn
																	devType ="Transformer";
																	dev = trafoEnd.name;
																	term = 4;
																}
																multiR+=1;
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}						
					}
				}
				//ZB = Math.pow(VB,2)/SB; //Calculate base impedance at node.
				//VBn = trafoEnd.VBn; //Get nominal base voltage of the Transformer.
				//SBn = trafoEnd.SBn; //Get nominal base power of the Transformer.
				//ZBn = Math.pow(VBn,2)/SBn; //Calculate base impedance at node.
				//From[] = busbar.name; //From bus.
				//R = trafoEnd.rtot/ZB; //Per unit resistance. *ZBn
				//X = trafoEnd.xtot/ZB; //Per unit reactance.	*ZBn
				
			}	
			devType ="Transformer";
			dev = trafo.name;
			R=2*R/multiL/multiR;
			X=2*X/multiL/multiR;
			for (int temp_i=0; temp_i<multiL; temp_i++) {
				notfound = true;
				for (int temp_j=0; temp_j<multiR; temp_j++) {
					for (Ybus ybus_temp : ybus_list) {
						if ((ybus_temp.From.equals(From[temp_i])) && (ybus_temp.To.equals(To[temp_j])) && (ybus_temp.dev.equals(dev))){
							notfound = false;
						}						
					}
					if (notfound) {
						ybus_list.add(new Ybus(From[temp_i],To[temp_j],R,X,0.0,0.0,devType,dev)); //Add branch to Y-Bus.
					}	
				}	
			}
			multiL=0;
			multiR=0;
			term=1;
		}	
	}
	
	//*** SHUNT COMPENSATOR ALGORITHM ***
	public static void scomp_search(Double SB, 
			ArrayList<ShuntCompensator> scomp_list,
			ArrayList<Terminal> terminal_list,
			ArrayList<ConnectivityNode> cnode_list,
			ArrayList<BusbarSection> busbar_list,
			ArrayList<VoltageLevel> voltlvl_list,
			ArrayList<BaseVoltage> basevolt_list,
			ArrayList<Ybus> ybus_list) {
		
		//Search algorithm to connect compensators to buses:
		//Compensator => Terminal => CNode => Terminal => Busbar		
		String From = null, To = null;
		String devType = null, dev = null;
		Double G, B;
		Double VB = null; //Voltage base (kV).
		Double YB = null; //Admittance base (ohm).
		
		for (ShuntCompensator scomp : scomp_list) {
			for (Terminal terminal1 : terminal_list) {
				if (scomp.id.equals(terminal1.ConductingEquipment)) {			
					for (ConnectivityNode cnode : cnode_list) {
						if (cnode.id.equals(terminal1.ConnectivityNode)) {
							for (Terminal terminal2 : terminal_list) {
								if (!terminal2.id.equals(terminal1.id) && cnode.id.equals(terminal2.ConnectivityNode)) {									
									for (BusbarSection busbar : busbar_list) {
										if (busbar.id.equals(terminal2.ConductingEquipment)) {														
											VB = busbar.getBaseVoltage(voltlvl_list,basevolt_list); //Get base voltage of bus.
											YB = 1/Math.pow(VB,2)*SB; //Calculate base admittance at node.
											From = "Ground";
											To = busbar.name; //To bus.
											G = scomp.gs/YB; //Per unit conductance.
											B = scomp.bs/YB; //Per unit susceptance.																			
											devType="Shunt Capacitor";
											dev = scomp.name;
											ybus_list.add(new Ybus(From,To,0.0,0.0,G,B,devType,dev)); //Add branch to Y-Bus.																				
										}
									}																			
								}
							}
						}
					}
				}
			}	
		}	
	}
}
